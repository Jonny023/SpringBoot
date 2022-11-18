package com.example.demo.entity;

import org.springframework.stereotype.Component;

/**
 *  上传进度
 * @Author Lee
 * @Date 2019年05月05日 20:17
 */
@Component
public class UploadProgress {

    private long readBytes = 0L; //到目前为止读取文件的比特数
    private long allBytes = 0L;    //文件总大小
    private int currentIndex;      //目前正在读取第几个文件

    public long getReadBytes() {
        return readBytes;
    }

    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    public long getAllBytes() {
        return allBytes;
    }

    public void setAllBytes(long allBytes) {
        this.allBytes = allBytes;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    public String toString() {
        return "UploadProgress{" +
                "readBytes=" + readBytes +
                ", allBytes=" + allBytes +
                ", currentIndex=" + currentIndex +
                '}';
    }
}
