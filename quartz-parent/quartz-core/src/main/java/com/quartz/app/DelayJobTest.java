package com.quartz.app;

import com.quartz.job.DelayJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * job实例： 一个任务正常执行为5秒/次，即：0s 5s 10s 15s ...
 * 假如执行超过5s，会不会影响之后的job
 */
public class DelayJobTest {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 任务实例
        JobDetail jobDetail = JobBuilder.newJob(DelayJob.class)
                .withIdentity("helloJob", "helloGroup")
                .build();

        System.out.println(jobDetail.isConcurrentExectionDisallowed());
        System.out.println(jobDetail.isPersistJobDataAfterExecution());

        // 触发器
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("helloTrigger", "triggerGroup")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(3))
                .startNow()
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        // 启动调度器 启动和调度器关联数序可以调换
        scheduler.start();

    }

}
