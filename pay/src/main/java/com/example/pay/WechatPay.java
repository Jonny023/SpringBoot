package com.example.pay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.config.WechatPayConfiguration;
import com.example.domain.dto.OrderPayDTO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;
import com.example.exctption.PayException;
import com.example.pay.enums.BooleanEnum;
import com.example.pay.wx.OrderModel;
import com.google.common.base.Splitter;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Component
public class WechatPay implements Pay {

    private final Logger log = LoggerFactory.getLogger(WechatPay.class);

    @Lazy
    @Resource
    private HttpClient weChartHttpClient;

    @Lazy
    @Resource
    private WechatPay2Validator wechatPay2Validator;

    @Autowired
    private PrivateKeySigner privateKeySigner;

    @Autowired
    private AesUtil aesUtil;

    @Autowired
    private AutoUpdateCertificatesVerifier autoUpdateCertificatesVerifier;

    @Lazy
    @Resource
    private WechatPayConfiguration config;

    @Override
    public OrderPayDTO createOrder(ShopDTO param) {
        //TODO 需要验证响应结果 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml
        JSONObject result = this.createOrderRequest(param.getOutTradeNo(), param.getDescription(), param.getAttach(), param.getTotal(), config.getCreateUrl());
        //成功
        if (result.containsKey("prepay_id")) {
            String prepayId = result.get("prepay_id").toString();
            String nonce = RandomUtil.randomString(16);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String message = config.getAppId() + "\n"
                    + timestamp + "\n"
                    + nonce + "\n"
                    + prepayId + "\n";

            PrivateKey privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(config.getMerchantPrivateKey().getBytes(StandardCharsets.UTF_8)));
            Signature signature = null;
            try {
                signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(privateKey);
                signature.update(message.getBytes(StandardCharsets.UTF_8));
                return OrderPayDTO.builder()
                        .appid(config.getAppId())
                        .partnerid(config.getMerchantId())
                        .prepayid(prepayId)
                        ._package("Sign=WXPay")
                        .noncestr(nonce)
                        .timestamp(timestamp)
                        .sign(Base64.getEncoder().encodeToString(signature.sign()))
                        .build();
            } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                throw new PayException(e);
            }
        } else if (result.containsKey("code")) {
            throw new PayException("订单生成失败: " + result.get("message").toString());
        } else {
            log.error("微信支付未知错误----> {} ", result.toJSONString());
            throw new PayException("未知错误");
        }
    }

    private JSONObject createOrderRequest(String outTradeNo, String description, String attach, int total, String requestUrl, String... ip) {
        OrderModel orderModel = OrderModel.builder()
                .appid(config.getAppId())
                .mchid(config.getMerchantId())
                .description((description != null && description.length() > 30) ? description.substring(0, 30) : description)
                .out_trade_no(outTradeNo)
                .time_expire(DateUtil.format(LocalDateTime.now().plusMinutes(30), "YYYY-MM-dd'T'HH:mm:ss+08:00"))
                .attach(attach)
                .notify_url(config.getNotifyUrl())
                .amount(OrderModel.Amount.builder().total(total).build())
                .build();
        if (ArrayUtil.isNotEmpty(ip)) {
            orderModel.setScene_info(OrderModel.SceneInfo.builder()
                    .payer_client_ip(ip[0])
                    .h5_info(OrderModel.H5Info.builder().type("Wap").build()).build());
        }
        String bodyAsString = "";
        try {
            CloseableHttpResponse response = this.requestBuild(requestUrl, orderModel);
            log.info("创建微信订单: {}", response);
            if (this.wechatPay2Validator.validate(response)) {
                bodyAsString = EntityUtils.toString(response.getEntity());
            } else {
                throw new PayException("微信订单创建失败, 未通过安全验证");
            }
        } catch (PayException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PayException("微信订单创建失败", e);
        }
        return JSON.parseObject(bodyAsString);
    }

    /**
     * 构建基础post请求体
     */
    private CloseableHttpResponse requestBuild(String url, Object body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.removeHeaders("Authorization");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(body), "UTF-8"));
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) weChartHttpClient.execute(httpPost);
            if (response.getEntity() != null) {
                log.error(EntityUtils.toString(response.getEntity()));
            }
            log.error("response code : {}", response.getStatusLine().getStatusCode());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PayException("微信请求发起失败");
        }
    }

    private String getParam(String url, String name) {
        String params = url.substring(url.indexOf("?") + 1);
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(name);
    }

    /**
     * H5支付
     *
     * @param outTradeNo  商户订单号
     * @param description 商品描述
     * @param attach      随机码用于回调验证
     * @param total       商品总额,分
     * @param ip
     */
    public PcPayDTO createH5Order(String outTradeNo, String description, String attach, int total, String ip) {

        JSONObject result = this.createOrderRequest(outTradeNo, description, attach, total, config.getH5CreateUrl(), ip);
        //成功
        if (result.containsKey("h5_url")) {
            String prepayId = this.getParam(result.get("h5_url").toString(), "prepay_id");
            return PcPayDTO.builder()
                    .payCode(result.get("h5_url").toString())
                    .prepayid(prepayId)
                    .build();
        } else if (result.containsKey("code")) {
            throw new PayException("订单创建失败" + result.get("message").toString());
        } else {
            log.error("微信支付未知错误: {} ", result.toJSONString());
            throw new PayException("未知异常");
        }
    }

    /**
     * native支付(二维码)
     *
     * @param outTradeNo  商户订单号
     * @param description 商品描述
     * @param attach      随机码用于回调验证
     * @param total       商品总额,分
     */
    public PcPayDTO createNativeOrder(String outTradeNo, String description, String attach, int total) {
        JSONObject result = this.createOrderRequest(outTradeNo, description, attach, total, config.getNativeCreateUrl());
        //成功
        if (result.containsKey("code_url")) {
            String codeUrl = result.get("code_url").toString();
            return PcPayDTO.builder()
                    .payCode(QrCodeUtil.generateAsBase64(codeUrl, QrConfig.create().setWidth(400).setHeight(400), ImgUtil.IMAGE_TYPE_PNG))
                    .prepayid(codeUrl)
                    .build();
        } else if (result.containsKey("code")) {
            throw new PayException("订单创建失败" + result.get("message").toString());
        } else {
            log.error("微信支付未知错误----> {} ", result.toJSONString());
            throw new PayException("未知错误");
        }
    }

    @Override
    public PcPayDTO pay(ShopDTO param) {
        log.info("微信支付");
        PcPayDTO pcPayDTO;
        //h5
        if (param.getH5() == BooleanEnum.TRUE) {
            pcPayDTO = this.createH5Order(param.getOutTradeNo(), param.getSubject(), param.getAttach(), param.getTotal(), param.getRemoteAddr());
        } else {
            //native
            pcPayDTO = this.createNativeOrder(param.getOutTradeNo(), param.getSubject(), param.getAttach(), param.getTotal());
        }
        return pcPayDTO;
    }

    @Override
    public boolean closeOrder(String outTradeNo) {
        JSONObject closeModel = new JSONObject();
        closeModel.put("mchid", config.getMerchantId());
        CloseableHttpResponse response = this.requestBuild(config.getCloseUrlPrefix() + outTradeNo + config.getCloseUrlSuffix(), closeModel);
        try {
            if (!this.wechatPay2Validator.validate(response)) {
                throw new PayException("微信支付订单关闭未通过安全验证");
            }
            if (response.getStatusLine().getStatusCode() != 204) {
                String bodyAsString = EntityUtils.toString(response.getEntity());
                JSONObject result = JSON.parseObject(bodyAsString);
                //如果当前响应中没有包含code,或code 不表示订单已关闭错误
                if (!result.containsKey("code") || !result.get("code").toString().equals("ORDER_CLOSED")) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PayException("订单关闭失败");
        }
        return true;
    }
}
