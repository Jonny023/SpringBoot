package com.example.javacv.factory.converter;

import com.example.javacv.converter.Converter;
import com.example.javacv.converter.FFmpegFrameConverter;
import com.example.javacv.factory.thread.ConverterThreadPoolFactory;
import com.example.javacv.factory.thread.SingletonConverterThreadPoolFactory;

/**
 * ffmpegFrame 转换器工厂
 */
public class FFmpegFrameConverterFactory extends AbstractThreadPoolConverterFactory {

    public FFmpegFrameConverterFactory() {
        super(new SingletonConverterThreadPoolFactory());
    }
    public FFmpegFrameConverterFactory(ConverterThreadPoolFactory converterThreadPoolFactory) {
        super(converterThreadPoolFactory);
    }

    @Override
    public Converter getConverter(String src, String out) {
        return new FFmpegFrameConverter(src, out, converterThreadPoolFactory.getThreadPoolExecutor());
    }
}
