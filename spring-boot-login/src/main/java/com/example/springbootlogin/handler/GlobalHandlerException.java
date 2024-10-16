package com.example.springbootlogin.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springbootlogin.domain.vo.R;
import com.example.springbootlogin.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BizException.class)
    public R bizError(BizException e) {
        log.error("", e);
        String message = e.getMsg();
        if (StrUtil.isEmpty(message)) {
            message = ExceptionUtil.getMessage(e);
        }
        return R.errorMsg(message);
    }

    @ExceptionHandler(Exception.class)
    public R globalError(Exception e) {
        log.error("", e);
        String message = ExceptionUtil.getMessage(e);
        return R.errorMsg(message);
    }
}
