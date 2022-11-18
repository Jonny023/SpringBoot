package com.example.javacv.factory.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 单例线程池工厂
 */
public class SingletonConverterThreadPoolFactory extends AbstractConverterThreadPoolFactory {

    private static volatile ThreadPoolExecutor executor;

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        if(executor == null) {
            synchronized (SingletonConverterThreadPoolFactory.class) {
                if (executor == null) {
                    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10, threadFactory);
                }
            }
        }
        return executor;
    }

}
