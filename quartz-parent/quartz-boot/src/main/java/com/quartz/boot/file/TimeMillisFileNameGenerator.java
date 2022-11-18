package com.quartz.boot.file;

/**
 * @Author: Wuyulin
 * @Date: 2021/8/26 10:26
 */
public class TimeMillisFileNameGenerator implements FileNameGenerator{

    @Override
    public String generator() {
        return String.valueOf(System.currentTimeMillis());
    }
}
