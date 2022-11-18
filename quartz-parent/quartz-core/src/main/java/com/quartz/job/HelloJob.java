package com.quartz.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class HelloJob implements Job {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static int i = 0;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String currentDate = formatter.format(LocalDateTime.now());
        i++;
        /*if(i==1) {
            JobExecutionException jobExecutionException = new JobExecutionException();
            // 这个工作将立即重新开始
            //jobExecutionException.setRefireImmediately(true);
            throw jobExecutionException;
        }*/

        log.info("HelloJob running:{}, times:{}", currentDate, i);
        if(i >= (Integer) context.getMergedJobDataMap().get("total")) {
            //关闭任务
            context.getScheduler().unscheduleJob(context.getTrigger().getKey());
            //context.getScheduler().deleteJob(context.getJobDetail().getKey());
        }
    }
}
