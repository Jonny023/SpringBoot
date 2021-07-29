package com.quartz.app;

import com.quartz.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 任务实例
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJob", "helloGroup")
                .build();

        // 触发器
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("helloTrigger", "triggerGroup")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .startNow()
                .build();

        // 关联任务实例和触发器
        scheduler.scheduleJob(jobDetail, trigger);

        // 启动调度器
        scheduler.start();

        // 停止
        //scheduler.shutdown();
    }
}
