package com.example.task.file;


import java.time.Instant;

public class FileNameManualGenerator implements FileNameGenerator {

    private String oldName;

    public FileNameManualGenerator(String oldName) {
        this.oldName = oldName;
    }

    @Override
    public String generator() {
        return String.format("%s_%s", oldName, Instant.now().toEpochMilli());
    }
}