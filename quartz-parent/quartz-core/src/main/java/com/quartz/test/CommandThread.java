package com.quartz.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;

public class CommandThread implements Callable<String> {

    private final Logger LOG = LoggerFactory.getLogger(CommandThread.class);

    InputStream is;
    String type;
    OutputStream os;

    CommandThread(InputStream is, String type) {
        this(is, type, null);
    }

    CommandThread(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    @Override
    public String call() {
        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
