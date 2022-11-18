package com.example.pay.api;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.example.domain.dto.OrderPayVO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;
import com.example.exctption.PayException;
import com.example.pay.Pay;
import com.example.pay.PaymentContext;
import com.example.pay.enums.BooleanEnum;
import com.example.pay.enums.PayType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class PayServiceImpl implements PayService {

    private final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

    @Resource
    private PaymentContext paymentContext;

    @Override
    public String createOrder(HttpServletRequest request) {
        ShopDTO param = new ShopDTO();
        param.setAttach(RandomUtil.randomString(10));
        param.setOutTradeNo(RandomUtil.randomString(10));
        param.setSubject("iphone 13");
        param.setDescription("iphone 13 128GB");
        param.setOutTradeNo(generateTradeNo());
        param.setH5(BooleanEnum.TRUE);
        //param.setPayType(PayType.ALI_PAY);
        param.setPayType(PayType.WECHAT_PAY);
        param.setTotal(1);
        param.setRemoteAddr(request.getRemoteAddr());

        log.info("创建三方订单: {}", JSON.toJSONString(param));
        Pay pay = paymentContext.build(param);
        pay.createOrder(param);
        return param.getOutTradeNo();
    }

    /**
     * redis自增订单号
     *
     * @return
     */
    private String generateTradeNo() {
        return "100001";
        //return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) +
        //        RandomUtil.randomNumbers(6) +
        //        String.format("%06d", cacheCore.inc(CacheKey.OUT_TRADE_NUMBER_KEY, 1L) % 1000000);
    }

    /**
     * PC发起支付
     */
    @Override
    public PcPayDTO pay(OrderPayVO vo) {
        //TODO 分布式锁
        try {
            log.info("判断订单是否关闭");
            //判断订单是否关闭
            //boolean closed = weChatPayUtil.closeOrder(thirdpayOrder.getOutTradeNo());
            //boolean closed = aliPayUtil.closeOrder(thirdpayOrder.getOutTradeNo());

            //为了测试，这里通过request的请求头动态传参
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();

            ShopDTO param = new ShopDTO();
            param.setAttach(request.getHeader("attach"));
            param.setOutTradeNo(request.getHeader("outTradeNo"));
            param.setSubject("iphone 13");
            param.setDescription("iphone 13 128GB");
            param.setOutTradeNo(generateTradeNo());
            param.setH5(vo.getH5());
            param.setPayType(vo.getPayType());
            param.setTotal(1);
            PcPayDTO orderPayTO = null;

            log.info("创建三方订单");
            Pay pay = paymentContext.build(param);
            PcPayDTO pcPayDTO = pay.pay(param);

            log.info("支付结果: {}", pcPayDTO);

            log.info("订单入库");

            return orderPayTO;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发起支付失败: ", e);
            throw new PayException("订单创建失败", e);
        }
        //释放分布式锁
    }
}
