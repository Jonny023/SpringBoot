package com.example.javacv.converter;

import com.example.javacv.exception.ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public abstract class AbstractRuntimeConverter implements Converter {

    Logger log = LoggerFactory.getLogger(AbstractRuntimeConverter.class);

    private String workId = UUID.randomUUID().toString().replaceAll("-","");

    private boolean isStart = false;

    Process process;

    @Override
    public void start() throws ConverterException {
        String cmd = cmd();
        Runtime run = Runtime.getRuntime();
        try {
            log.info(cmd);
            Process process = run.exec(cmd);
            isStart = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (isStart) {
            process.exitValue();
            isStart = false;
        }
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public String getWorkId() {
        return workId;
    }

    protected abstract String cmd();

    protected abstract void result(String result);
}
