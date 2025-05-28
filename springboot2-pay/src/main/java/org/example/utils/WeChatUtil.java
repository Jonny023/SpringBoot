package org.example.utils;

import cn.hutool.core.date.DateUtil;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.cipher.SignatureResult;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.core.util.NonceUtil;
import com.wechat.pay.java.core.util.PemUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import static cn.hutool.core.date.DatePattern.PURE_DATETIME_FORMATTER;

public class WeChatUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成订单号
     *
     * @param
     * @return String
     * @author shy
     * @date 2024/4/8 16:15
     */
    public static String generateTradeNumber() {
        // 定义订单号前缀
        String prefix = "shy";
        // 当前年月日
        // String currentTimeStr = DateUtil.formatDateTime().getCurrent("yyyyMMddHHmmss");
        String currentTimeStr = DateUtil.format(new Date(), PURE_DATETIME_FORMATTER);
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        // 构造订单号
        return prefix + currentTimeStr + timestamp;
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @param
     * @return String
     * @author shy
     * @date 2024/4/16 17:07
     */
    public static String generateNonceStr() {
        return NonceUtil.createNonce(32);
    }

    /**
     * 获取当前时间戳，单位秒
     *
     * @param
     * @return long
     * @author shy
     * @date 2024/4/16 17:10
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getSign(String signatureStr, String privateKeyPath, String merchantSerialNumber) {
        PrivateKey privateKey = PemUtil.loadPrivateKeyFromPath(privateKeyPath);
        RSASigner rsaSigner = new RSASigner(merchantSerialNumber, privateKey);
        SignatureResult signatureResult = rsaSigner.sign(signatureStr);
        return signatureResult.getSign();
    }


    /**
     * 构造 RequestParam
     *
     * @param request
     * @return RequestParam
     * @author shy
     * @date 2024/4/9 11:16
     */
    public static RequestParam handleNodifyRequestParam(HttpServletRequest request) throws IOException {
        // 请求头Wechatpay-Signature
        String signature = request.getHeader("Wechatpay-Signature");
        // 请求头Wechatpay-nonce
        String nonce = request.getHeader("Wechatpay-Nonce");
        // 请求头Wechatpay-Timestamp
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 微信支付证书序列号
        String serial = request.getHeader("Wechatpay-Serial");
        // 签名方式
        String signType = request.getHeader("Wechatpay-Signature-Type");
        // 构造 RequestParam
        return new RequestParam.Builder().serialNumber(serial).nonce(nonce).signature(signature).timestamp(timestamp).signType(signType).body(getRequestBody(request)).build();

    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        ServletInputStream stream;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new IOException("读取返回支付接口数据流出现异常！");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }
}
