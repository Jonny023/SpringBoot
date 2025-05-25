package org.example.controller;

import com.google.common.collect.Lists;
import io.github.lnyocly.ai4j.listener.SseListener;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatCompletion;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatCompletionResponse;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatMessage;
import io.github.lnyocly.ai4j.service.IChatService;
import io.github.lnyocly.ai4j.service.PlatformType;
import io.github.lnyocly.ai4j.service.factor.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Jonny
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class DeepSeekChatController {

    @Value("${ai.model}")
    private String modelName;
    @Value("${ai.platform}")
    private String platform;

    // 注入Ai服务
    @Resource
    private AiService aiService;

    /**
     * 普通消息
     *
     * @param q 问题
     * @return 回答内容
     */
    @GetMapping("/chat")
    public String getChatMessage(@RequestParam String q) throws Exception {
        // 获取OLLAMA的聊天服务
        IChatService chatService = aiService.getChatService(PlatformType.getPlatform(platform));
        // 创建请求参数
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(modelName)
                .message(ChatMessage.withUser(q))
                .build();
        // 发送chat请求
        ChatCompletionResponse chatCompletionResponse = chatService.chatCompletion(chatCompletion);
        // 获取聊天内容和token消耗
        String content = chatCompletionResponse.getChoices().get(0).getMessage().getContent().getText();
        long totalTokens = chatCompletionResponse.getUsage().getTotalTokens();
        System.out.println("总token消耗:" + totalTokens);
        return content;
    }

    /**
     * 流式响应
     *
     * @param q        问题
     * @param response 响应
     */
    @GetMapping("/chatStream")
    public void getChatMessageStream(@RequestParam String q, HttpServletResponse response) throws Exception {
        // 中文乱码问题
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        // 获取OpenAi的聊天服务
        IChatService chatService = aiService.getChatService(PlatformType.getPlatform(platform));

        // 创建请求参数
        List<ChatMessage> messages = Lists.newArrayList();
        messages.add(ChatMessage.withSystem("你是一个Java技术专家，精通各种框架技术"));
        ChatMessage message = ChatMessage.withAssistant(q);
        messages.add(message);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(modelName)
                .messages(messages)
                .build();


        PrintWriter writer = response.getWriter();
        // 发送chat请求
        SseListener sseListener = new SseListener() {
            @Override
            protected void send() {
                writer.write(this.getCurrStr());
                writer.flush();
                // System.out.println(this.getCurrStr());
            }

        };

        // 显示函数参数，默认不显示
        sseListener.setShowToolArgs(true);

        chatService.chatCompletionStream(chatCompletion, sseListener);
        writer.close();
        // System.out.println(sseListener.getOutput());

    }

}

