package com.example.javacv.factory.converter;

import com.example.javacv.converter.Converter;

public interface ConverterFactory {

    Converter getConverter(String src, String out);
}
