package com.example.javacv.factory.converter;

import com.example.javacv.converter.Converter;
import com.example.javacv.converter.FFmpegRuntimeConverter;

public class FFmpegRuntimeConverterFactory implements ConverterFactory{

    @Override
    public Converter getConverter(String src, String out) {
        return new FFmpegRuntimeConverter(src, out);
    }
}
