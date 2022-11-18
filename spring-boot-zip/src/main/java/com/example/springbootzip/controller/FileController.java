package com.example.springbootzip.controller;

import com.example.springbootzip.utils.ZipUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Controller
public class FileController {

    @GetMapping("/file/download")
    public void download(HttpServletResponse response) {
        List<File> files = Arrays.asList(
            new File("I:\\zip\\readme.txt")
            ,new File("I:\\zip\\新建 DOCX 文档.docx")
            ,new File("I:\\zip\\新建 WinRAR 压缩文件.rar")
        );
        ZipUtil.generateZip(response, files);
    }
}
