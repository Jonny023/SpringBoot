package com.example.springbootminiapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/wx")
public class WxController {

    @Resource
    private WxMaService wxMaService;

    /**
     * 获取手机号
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/getUserPhoneNumber")
    public Object getUserPhoneNumber(String code) throws WxErrorException {
        return wxMaService.getUserService().getPhoneNoInfo(code);
    }

    @GetMapping("/getAccessToken")
    public Object getAccessToken() throws WxErrorException {
        return wxMaService.getAccessToken();
    }

    @GetMapping("/login")
    public Object login(String phone) {
        log.info("param: {}", phone);
        return "success";
    }
}
