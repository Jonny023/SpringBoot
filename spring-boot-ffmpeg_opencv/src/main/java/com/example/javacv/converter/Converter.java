package com.example.javacv.converter;


import com.example.javacv.exception.ConverterException;

/**
 * 转流器方法定义
 */
public interface Converter {

    /**
     * 开启转流
     */
    void start() throws ConverterException;

    /**
     * 关闭转流
     */
    void close();

    /**
     * 是否已开启转流
     * @return
     */
    boolean isStart();

    /**
     * 获取转流编号(用作唯一标识)
     * @return
     */
    String getWorkId();

}
