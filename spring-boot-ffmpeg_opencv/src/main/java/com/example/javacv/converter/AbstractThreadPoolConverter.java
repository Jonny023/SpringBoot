package com.example.javacv.converter;

import com.example.javacv.exception.ConverterException;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractThreadPoolConverter implements Converter {

    private String workId = UUID.randomUUID().toString().replaceAll("-","");

    private boolean isStart = false;

    private Thread currentThread;

    protected ThreadPoolExecutor executor;

    public AbstractThreadPoolConverter(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void start() throws ConverterException {
        try {
            isStart = true;
            executor.execute(() -> {
                currentThread = Thread.currentThread();
                convert();
                isStart = false;
            });
        } catch (Exception e) {
            isStart = false;
        }
    }

    @Override
    public void close() {

        if(currentThread != null) {
            //暂时用stop, interrupt会有业务侵入性
            currentThread.interrupt();
//            currentThread.stop();
            isStart = false;
        }
    }

    @Override
    public boolean isStart() {
        return this.isStart;
    }

    @Override
    public String getWorkId() {
        return this.workId;
    }

    protected boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }

    protected abstract void convert();
}
