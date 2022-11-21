package com.example.controller;

import com.example.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/wx")
public class WeChatController {

    @Resource
    private WechatService wechatService;

    /**
     * 接入token验证
     * 非必选项（若消息要回调可以配置此接口）
     *
     * @param signature 微信加密签名，signature结合了开发者填写的 token 参数和请求中的 timestamp 参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return
     */
    @RequestMapping(value = "/checkToken", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkToken(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam(value = "echostr", required = false) String echostr) {
        return wechatService.checkToken(signature, timestamp, nonce, echostr);
    }

    /**
     * 根据模板发送消息
     *
     * @return
     */
    @PostMapping("/sendTemplateMsg")
    public String sendTemplateMsg() {
        WxMpTemplateMessage template = new WxMpTemplateMessage();
        template.setTemplateId("xxx");
        template.setToUser("xxx");
        List<WxMpTemplateData> wxMpTemplateDataList = new ArrayList<>();
        WxMpTemplateData data = new WxMpTemplateData("thing9", "张三", "#ff0000");
        wxMpTemplateDataList.add(data);
        data = new WxMpTemplateData("thing10", "您的预约已成功取消，请重新进行预约", "#ff0000");
        wxMpTemplateDataList.add(data);
        template.setData(wxMpTemplateDataList);
        return wechatService.sendTemplateMsg(template);
    }

    /**
     *  发送小程序订阅消息
     * @return
     */
    @PostMapping("/send")
    public String send() {
        WxMpSubscribeMessage message = new WxMpSubscribeMessage();
        message.setTemplateId("xxx");
        message.setToUser("xxx");

        Map<String, String> map = new HashMap<>();
        map.put("thing9", "张三");
        map.put("thing10", "您的预约已成功取消，请重新进行预约");
        message.setDataMap(map);

        //message.setPage("pages/selectHospital/selectHospital");
        WxMpSubscribeMessage.MiniProgram miniProgram = new WxMpSubscribeMessage.MiniProgram();
        miniProgram.setAppid("xxx");
        miniProgram.setPagePath("pages/selectHospital/selectHospital");
        miniProgram.setUsePath(true);
        message.setMiniProgram(miniProgram);

        wechatService.send(message);
        return "success";
    }


    /**
     *  获取小程序消息模板
     * @return
     */
    @PostMapping("/getTemplateList")
    public List<TemplateInfo> getTemplateList() {
        return wechatService.getTemplateList();
    }
}