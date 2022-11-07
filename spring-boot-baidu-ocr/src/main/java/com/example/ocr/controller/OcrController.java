package com.example.ocr.controller;

import com.example.ocr.controller.domain.vo.BankResponseVO;
import com.example.ocr.controller.domain.vo.IdCardResponseVO;
import com.example.ocr.controller.domain.vo.R;
import com.example.ocr.controller.service.BaiduOcrService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * https://blog.51cto.com/u_14983647/5615565
 */
@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Resource
    private BaiduOcrService baiduOcrService;

    @PostMapping("/idCard")
    public R<IdCardResponseVO> idCard(@RequestPart("file") MultipartFile file) {
        return R.ok(baiduOcrService.idCard(file));
    }

    @PostMapping("/bankCard")
    public R<BankResponseVO> bankCard(@RequestPart("file") MultipartFile file) {
        return R.ok(baiduOcrService.bankCard(file));
    }
}
