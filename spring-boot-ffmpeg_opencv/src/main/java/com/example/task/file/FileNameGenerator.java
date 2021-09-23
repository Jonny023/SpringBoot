package com.example.task.file;

import java.io.Serializable;

/**
 * @Author: Wuyulin
 * @Date: 2021/8/26 10:24
 */
@FunctionalInterface
public interface FileNameGenerator extends Serializable {
    String  generator();
}
