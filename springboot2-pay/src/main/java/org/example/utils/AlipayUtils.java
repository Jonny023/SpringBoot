package org.example.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.example.config.AlipayConfig;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝工具类
 */
@Component
public class AlipayUtils {

    /**
     * 生成订单号
     *
     * @return String
     */
    public String getOrderCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int a = (int) (Math.random() * 9000.0D) + 1000;
        System.out.println(a);
        Date date = new Date();
        String str = sdf.format(date);
        String[] split = str.split("-");
        String s = split[0] + split[1] + split[2];
        String[] split1 = s.split(" ");
        String s1 = split1[0] + split1[1];
        String[] split2 = s1.split(":");
        return split2[0] + split2[1] + split2[2] + a;
    }

    /**
     * 校验签名
     *
     * @param request HttpServletRequest
     * @param alipay  阿里云配置
     * @return boolean
     */
    public boolean rsaCheck(HttpServletRequest request, AlipayConfig alipay) {

        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(1);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String o : requestParams.keySet()) {
            String[] values = requestParams.get(o);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(o, valueStr);
        }

        try {
            return AlipaySignature.rsaCheckV1(params,
                    alipay.getPublicKey(),
                    alipay.getCharset(),
                    alipay.getSignType());
        } catch (AlipayApiException e) {
            return false;
        }
    }
}

