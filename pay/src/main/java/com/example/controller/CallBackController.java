package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.domain.response.PayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class CallBackController {
    private Logger log = LoggerFactory.getLogger(CallBackController.class);

    /**
     * 支付宝回调
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @PostMapping("/ali_pay")
    public void aliPay(@RequestParam Map<String, String> params, HttpServletResponse response) throws Exception {
        log.info("支付宝回调接收");
        if (!"TRADE_SUCCESS".equals(params.get("trade_status")) && !"TRADE_FINISHED".equals(params.get("trade_status"))) {
            throw new RuntimeException();
        }
        try {
            //存入mq消息中间件
            //this.senderUtil.send(PAYMENT_MESSAGE, new PaymentMessage(2, null, null, JSONObject.toJSONString(params), null, null));
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收支付宝回调失败");
            //todo: 通知运维人员
            throw e;
        }
    }

    @PostMapping("/wechat_pay")
    public PayResponse wechatPay(@RequestBody String data, HttpServletRequest request) throws Exception {
        log.info("微信回调接收");
        String sign = request.getHeader("Wechatpay-Signature");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        if (StringUtils.isEmpty(serial) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp)) {
            throw new RuntimeException();
        }
        try {
            //this.senderUtil.send(PAYMENT_MESSAGE, new PaymentMessage(1, timestamp, nonce, data, sign, serial));
            return new PayResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收微信支付回调失败");
            //todo: 通知运维人员
            throw e;
        }
    }

    @PostMapping("/mini_wechat_pay")
    public PayResponse miniWechatPay(@RequestBody String data, HttpServletRequest request) throws Exception {
        log.info("微信回调接收");
        String sign = request.getHeader("Wechatpay-Signature");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        if (StringUtils.isEmpty(serial) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp)) {
            throw new RuntimeException();
        }
        try {
            //this.senderUtil.send(MINI_PAYMENT_MESSAGE, new PaymentMessage(1, timestamp, nonce, data, sign, serial));
            return new PayResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收微信支付回调失败");
            //todo: 通知运维人员
            throw e;
        }
    }

    /**
     * 此接口非苹果支付回调，而是 苹果在退款等其他事件对服务器的主动推送消息
     * 参数结构参考
     * https://developer.apple.com/documentation/appstoreservernotifications/responsebody
     *
     */
    @RequestMapping(value = "/appleNotification", produces = "application/json;charset=UTF-8")
    public String appleNotification(@RequestBody JSONObject call) {
        log.info("apple 通知接收");
        try {
            //this.senderUtil.send(APPLE_MESSAGE, call.toJSONString());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收apple 通知 失败");
            //todo: 通知运维人员
            throw e;
        }
    }
}
