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

        // 启动调度器 启动和调度器关联数序可以调换
        scheduler.start();

        // 关联任务实例和触发器
        scheduler.scheduleJob(jobDetail, trigger);

        System.out.println(trigger.getJobKey());

        // 停止
        //scheduler.shutdown();
    }
}
