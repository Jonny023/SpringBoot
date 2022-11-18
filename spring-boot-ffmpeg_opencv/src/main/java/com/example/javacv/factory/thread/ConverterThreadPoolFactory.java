package com.example.javacv.factory.thread;

import java.util.concurrent.ThreadPoolExecutor;

public interface ConverterThreadPoolFactory {

    ThreadPoolExecutor getThreadPoolExecutor();

}
