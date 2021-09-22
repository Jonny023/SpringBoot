package com.quartz.boot.file;

import java.util.UUID;

/**
 * @Author: Wuyulin
 * @Date: 2021/8/26 10:30
 */
public class UUIDFileNameGenerator implements FileNameGenerator{

    @Override
    public String generator() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
