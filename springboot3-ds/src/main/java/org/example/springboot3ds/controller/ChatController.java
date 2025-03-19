package org.example.springboot3ds.controller;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author admin
 */
@RestController
public class ChatController {

    /**
     * 上下文
     */
    private final List<Message> contextHistoryList = new ArrayList<>();

    @Resource
    private OpenAiChatModel model;

    @PostConstruct
    public void init() {
        contextHistoryList.add(new SystemMessage("You are a Java technologist."));
    }

    /**
     * 普通对话
     *
     * @param message 问题
     * @return 回答结果
     */
    @GetMapping("/chat")
    public ChatResponse chat(String message) {
        contextHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(contextHistoryList);
        ChatResponse chatResp = model.call(prompt);
        Generation result = chatResp.getResult();
        if (Objects.nonNull(result) && Objects.nonNull(result.getOutput())) {
            contextHistoryList.add(result.getOutput());
        }
        return chatResp;
    }

    /**
     * 流式返回
     *
     * @param message 问题
     * @return 流式结果
     */
    @GetMapping("/chat/v1")
    public Flux<ChatResponse> chatV1(String message) {
        contextHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(contextHistoryList);
        return model.stream(prompt);
    }
}