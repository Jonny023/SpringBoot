package com.quartz.app;

import com.quartz.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 *  一个Job可以绑定多个Trigger
 *  一个Trigger只能绑定一个Job
 */
public class CronTest {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 任务实例
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJob", "helloGroup")
                .storeDurably() // 一个job绑定多个Trigger必须配置durability，否则报错：Jobs added with no trigger must be durable.
                .build();

        // 触发器
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(jobDetail) // 必须配置
                .withIdentity("cronTrigger", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();

        Trigger trigger1 = TriggerBuilder
                .newTrigger()
                .forJob(jobDetail) // 必须配置
                .withIdentity("cronTrigger1", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();

        // 必须配置
        scheduler.addJob(jobDetail, false);

        scheduler.scheduleJob(trigger);
        scheduler.scheduleJob(trigger1);
        scheduler.start();

        System.out.println(trigger.getJobKey());

        // 停止
        //scheduler.shutdown();
    }
}
