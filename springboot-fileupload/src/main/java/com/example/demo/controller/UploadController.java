package com.example.demo.controller;

import com.example.demo.entity.UploadProgress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 *
 * @Author Lee
 * @Date 2019年05月05日 20:30
 */
@Controller
public class UploadController {

    /**
     * 显示文件上传页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "home";
    }

    /*
     * 文件上传
     * <p>Title: upload</p>
     * <p>Description: </p>
     * @param file
     * @return
     */
    @PostMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        if (file != null && !file.isEmpty()) {
            try {
                File f = new File("d:/upload");
                if(!f.exists()) {
                    f.mkdirs();
                }
                String fileName = file.getOriginalFilename();
                String newFileName = UUID.randomUUID().toString().replaceAll("-","") + fileName.substring(fileName.lastIndexOf("."));
                System.out.println(newFileName);
                System.out.println(f.getAbsolutePath() + File.separator +newFileName);
                file.transferTo(new File(f.getAbsolutePath() + File.separator +newFileName));
                result.put("code", 200);
                result.put("msg", "success");
            } catch (IOException e) {
                result.put("code", -1);
                result.put("msg", "文件上传出错，请重新上传！");
                e.printStackTrace();
            }
        } else {
            result.put("code", -1);
            result.put("msg", "未获取到有效的文件信息，请重新上传!");
        }
        return result;
    }


    /**
     * 获取文件上传进度
     *
     * @param request
     * @return
     */
    @RequestMapping("getUploadProgress")
    @ResponseBody
    public UploadProgress getUploadProgress(HttpServletRequest request) {
        return (UploadProgress) request.getSession().getAttribute("uploadStatus");
    }
}
