package com.example.javacv;

/**
 * 转换器策略
 */
public enum ConvertStrategy {

    /**
     * javacv使用ffmpeg采流推流方案，使用线程池管理运行
     */
    FFMPEG_FRAME,

    /**
     * ffmpeg运行时执行命令
     */
    FFMPEG_RUNTIME;
}
