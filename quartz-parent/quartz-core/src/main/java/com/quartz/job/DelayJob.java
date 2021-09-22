package com.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DelayJob implements Job {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String currentDate = formatter.format(LocalDateTime.now());
        log.info("======延迟执行===========start:{}", currentDate);
        try {
            hello();
            Thread.sleep(6);
        } catch (InterruptedException e) {
            log.error("job运行异常.");
        }
        String endDate = formatter.format(LocalDateTime.now());
        log.info("======延迟执行===========end:{}", endDate);
    }

    public void hello() throws InterruptedException {
        System.out.println("数据备份......");
    }
}
