package com.example.javacv.factory.converter;

import com.example.javacv.factory.thread.ConverterThreadPoolFactory;

public abstract class AbstractThreadPoolConverterFactory implements ConverterFactory{

    protected ConverterThreadPoolFactory converterThreadPoolFactory;

    public AbstractThreadPoolConverterFactory(ConverterThreadPoolFactory converterThreadPoolFactory) {
        this.converterThreadPoolFactory = converterThreadPoolFactory;
    }

}
