package com.example.demo.listener;

import com.example.demo.entity.UploadProgress;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 *  上传进度监听类
 * @Author Lee
 * @Date 2019年05月05日 20:19
 */
@Component
public class UploadProgressListener implements ProgressListener {

    private HttpSession session;

    public void setSession(HttpSession session){
        this.session = session;
        UploadProgress uploadProgress = new UploadProgress();
        session.setAttribute("uploadStatus", uploadProgress);
    }


    /**
     *  设置当前已读取文件的进度
     * @param readBytes 已读的文件大小（单位byte）
     * @param allBytes 文件总大小（单位byte）
     * @param index 正在读取的文件序列
     */
    @Override
    public void update(long readBytes, long allBytes, int index) {
        UploadProgress uploadProgress = (UploadProgress) session.getAttribute("uploadStatus");
        uploadProgress.setReadBytes(readBytes);
        uploadProgress.setAllBytes(allBytes);
        uploadProgress.setCurrentIndex(index);
    }
}
