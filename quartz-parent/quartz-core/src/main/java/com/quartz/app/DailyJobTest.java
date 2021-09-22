package com.quartz.app;

import com.quartz.job.DelayJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 *  一段时间内执行n次
 */
public class DailyJobTest {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 任务实例
        JobDetail jobDetail = JobBuilder.newJob(DelayJob.class)
                .withIdentity("helloJob", "helloGroup")
                .build();

        // 触发器
        long endTime = 1630297800000L;
        long startTime = 1630294140000L;
        int intervalInMinutes = 5;
        int repeatCount = 5;
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("helloJob")
                .startNow()
                .endAt(new Date(endTime))
                .withSchedule(DailyTimeIntervalScheduleBuilder
                        .dailyTimeIntervalSchedule().onEveryDay()
                        .startingDailyAt(TimeOfDay.hourAndMinuteAndSecondFromDate(new Date(startTime)))
                        .withIntervalInSeconds(intervalInMinutes)
                        .endingDailyAfterCount(repeatCount))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        // 启动调度器 启动和调度器关联数序可以调换
        scheduler.start();

    }

}
