package org.example.service.impl;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.config.WxPayConfig;
import org.example.domain.common.R;
import org.example.domain.vo.WxPayTradeVO;
import org.example.service.WxPayService;
import org.example.utils.WeChatUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Slf4j
@Service("wxPay")
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private NativePayService nativePayService;

    @Resource
    private NotificationParser notificationParser;

    @Override
    public String nativePay(WxPayTradeVO wxPayTradeVO, HttpServletRequest request) {
        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setAppid(wxPayConfig.getAppId());
        prepayRequest.setMchid(wxPayConfig.getMerchantId());
        prepayRequest.setOutTradeNo(WeChatUtil.generateTradeNumber());
        prepayRequest.setDescription(wxPayTradeVO.getBody());
        prepayRequest.setNotifyUrl(wxPayConfig.getNotifyUrl());
        Amount amount = new Amount();
        amount.setTotal(wxPayTradeVO.getTotalAmount().multiply(new BigDecimal(100)).intValue());
        prepayRequest.setAmount(amount);
        try {
            PrepayResponse prepay = nativePayService.prepay(prepayRequest);
            // TODO 预支付成功，创建预支付订单
            return prepay.getCodeUrl();
        } catch (HttpException e) { // 发送HTTP请求失败
            log.error("发送HTTP请求失败: {}", e.getHttpRequest());
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            log.error("服务返回状态异常: {}", e.getResponseBody());
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("返回体类型不合法: {}", e.getMessage());
        } catch (Exception e) {
            log.error("预下单异常: {}", e.getMessage());
        }
        return null;
    }

    // @Transactional
    @Override
    public R payNotify(HttpServletRequest request) throws Exception {
        log.info("微信回调开始-------------------------");
        Transaction transaction;
        try {
            transaction = notificationParser.parse(WeChatUtil.handleNodifyRequestParam(request), Transaction.class);
            if (transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
                // 订单校验
                // 校验订单状态：若订单已支付则直接返回成功
                // 支付成功-修改订单状态
            }
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("签名验证失败: ", e);
            return R.error();
        }
        log.info("微信回调结束, 微信回调报文: {}", transaction);
        return R.ok();
    }

}
