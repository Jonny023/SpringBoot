package com.quartz.test;

import java.io.FileOutputStream;

public class CommandThreadTest {

    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("C:\\file.log");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("java jecho 'Hello World'");
            CommandThread cmdError = new CommandThread(proc.getErrorStream(), "ERROR");
            CommandThread cmdOutput = new CommandThread(proc.getInputStream(), "OUTPUT", fos);
            cmdError.call();
            cmdOutput.call();
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            fos.flush();
            fos.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
