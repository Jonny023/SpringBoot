package com.example.javacv;

import com.example.javacv.converter.Converter;
import com.example.javacv.exception.ConverterException;
import com.example.javacv.factory.converter.ConverterFactory;
import com.example.javacv.factory.converter.FFmpegFrameConverterFactory;
import com.example.javacv.factory.converter.FFmpegRuntimeConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 转流服务
 */
public class ConverterServer {

    private final static Logger log = LoggerFactory.getLogger(ConverterServer.class);

    /**
     * 转换器保存对象，用于管理转换器状态，例如重启，关闭
     * @key workId
     * @value converter
     * @see Converter
     */
    private static final ConcurrentHashMap<String, Converter> converterHolder = new ConcurrentHashMap();

    /**
     * 开启转换
     * @param src
     * @param out
     * @param convertStrategy
     * @return
     */
    public static String startConvert(String src, String out, ConvertStrategy convertStrategy) throws ConverterException {
        ConverterFactory converterFactory = switchFactory(convertStrategy);
        Converter converter = converterFactory.getConverter(src, out);
        //开启转换
        converter.start();
        converterHolder.put(converter.getWorkId(), converter);
        return converter.getWorkId();
    }

    /**
     * 关闭转换
     * @param workId
     */
    public static void closeConvert(String workId) {
        Converter converter = converterHolder.get(workId);
        if(converter != null) {
            converter.close();
        }
    }

    /**
     * 关闭所有转换
     */
    public static void closeAll() {
        converterHolder.forEach((workId, converter) -> converter.close());
    }

    /**
     * 转换策略选择
     * @param convertStrategy
     * @return
     */
    private static ConverterFactory switchFactory(ConvertStrategy convertStrategy) {
        switch (convertStrategy) {
            case FFMPEG_FRAME: return new FFmpegFrameConverterFactory();
            case FFMPEG_RUNTIME: return new FFmpegRuntimeConverterFactory();
            default: throw new ConverterException("转换策略未选择");
        }
    }
}
