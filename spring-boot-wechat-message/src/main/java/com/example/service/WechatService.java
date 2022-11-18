package com.example.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static me.chanjar.weixin.mp.enums.WxMpApiUrl.SubscribeMsg.SEND_MESSAGE_ONCE_URL;

@Slf4j
@Service
public class WechatService {

    @Resource
    private WxMpService wxMpService;

    /**
     * 接入token验证
     *
     * @param signature 微信加密签名，signature结合了开发者填写的 token 参数和请求中的 timestamp 参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return
     */
    public String checkToken(String signature, String timestamp, String nonce, String echostr) {
        log.info("signature:{}, timestamp:{}, nonce:{}, echostr: {}", signature, timestamp, nonce, echostr);
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息不合法
            return "消息不合法";
        }
        // 消息合法
        return echostr;
    }

    /**
     * 根据模板发送消息
     *
     * @param templateMessage
     */
    public String sendTemplateMsg(WxMpTemplateMessage templateMessage) {
        try {
            //List<TemplateInfo> templateList = wxMpService.getSubscribeMsgService().getTemplateList();
            return wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  发送小程序订阅消息【新版】
     *  sdk里面的接口是：/cgi-bin/message/subscribe/bizsend不能用，新接口：/cgi-bin/message/subscribe/send
     * @param message
     */
    public void send(WxMpSubscribeMessage message) {
        try {
            //wxMpService.getSubscribeMsgService().send(message);
            wxMpService.post(WxMpHostConfig.API_DEFAULT_HOST_URL + "/cgi-bin/message/subscribe/send", message.toJson());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取小程序消息模板
     * @return
     */
    public List<TemplateInfo> getTemplateList() {
        try {
            return wxMpService.getSubscribeMsgService().getTemplateList();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
