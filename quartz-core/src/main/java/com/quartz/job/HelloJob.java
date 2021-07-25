package com.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class HelloJob implements Job {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String currentDate = formatter.format(LocalDateTime.now());
        log.info("HelloJob run:{}", currentDate);
    }
}
