package com.example.pay;

import com.example.domain.dto.ShopDTO;
import com.example.pay.enums.PayType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentContext implements ApplicationContextAware {

    private Map<PayType, Pay> payProvider = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        payProvider.put(PayType.ALI_PAY, applicationContext.getBean(AliPay.class));
        payProvider.put(PayType.WECHAT_PAY, applicationContext.getBean(WechatPay.class));
        payProvider.put(PayType.UNION_PAY, applicationContext.getBean(UnionPay.class));
    }

    /**
     * 获取Pay接口
     *
     * @param param
     * @return
     */
    public Pay build(ShopDTO param) {
        return payProvider.get(param.getPayType());
    }

}
