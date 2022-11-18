package com.example.javacv.factory.thread;

import java.util.concurrent.ThreadFactory;

public abstract class AbstractConverterThreadPoolFactory implements ConverterThreadPoolFactory {

    protected ThreadFactory threadFactory;

    public AbstractConverterThreadPoolFactory() {
        this.threadFactory = new ConverterThreadFactory();
    }

    public AbstractConverterThreadPoolFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }
}
