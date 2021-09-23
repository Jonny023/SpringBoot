package com.example.javacv.factory.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 多例线程池工厂
 */
public class PrototypeConverterThreadPoolFactory extends AbstractConverterThreadPoolFactory {

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(10, threadFactory);
    }

}
