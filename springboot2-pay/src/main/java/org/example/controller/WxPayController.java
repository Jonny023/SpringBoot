package org.example.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.common.R;
import org.example.domain.vo.WxPayTradeVO;
import org.example.service.WxPayService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/wxpay")
public class WxPayController {

    @Resource
    private WxPayService wxPayService;

    @RequestMapping("/v3/nativePay")
    public R<String> nativePay(@RequestBody WxPayTradeVO wxPayTradeVO, HttpServletRequest request) {
        String nativeUrl = wxPayService.nativePay(wxPayTradeVO, request);
        if (StrUtil.isBlank(nativeUrl)) {
            return R.error("支付失败");
        }
        return R.ok(nativeUrl);
    }

    @RequestMapping("/v3/payNotify")
    public R payNotify(HttpServletRequest request) {
        try {
            return wxPayService.payNotify(request);
        } catch (Exception e) {
            log.error("微信回调异常: ", e);
            return R.error(e.getMessage());
        }
    }


}