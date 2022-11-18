package com.example.task.cv;


import com.example.task.file.FileNameGenerator;

public class CutDataDetail {

    /**
     *  任务id
     */
    private Long taskId;

    /**
     * 视频源
     */
    private String src;

    /**
     * 采集时间间隔，单位：秒
     */
    private int intervalSec;

    /**
     * 采集总数
     */
    private int total;

    /**
     * 图片文件名后缀，例如png, jpg
     */
    private String imageType;

    /**
     * 文件保存目录
     */
    private String fileDir;

    /**
     * 文件名生成器
     */
    private FileNameGenerator fileNameGenerator;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getIntervalSec() {
        return intervalSec;
    }

    public void setIntervalSec(int intervalSec) {
        this.intervalSec = intervalSec;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public FileNameGenerator getFileNameGenerator() {
        return fileNameGenerator;
    }

    public void setFileNameGenerator(FileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }
}
