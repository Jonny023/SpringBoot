package com.quartz.app;

import com.quartz.job.JobDetailIdentity;
import com.quartz.job.SingleJobDetailFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 每天安时段执行
 */
public class EveryHourRangeJobTest {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = SingleJobDetailFactory.getJobDetail(JobDetailIdentity.HELLO_JOB_TEST1);

        int repeatCount = 10; // 需执行的总次数
        int start = 13; // 几点开始
        int end = 18; // 几点结束
        int intervalInSeconds = 2; // 每隔n秒执行一次


        // withRepeatCount(repeatCount) 共执行repeatCount+1次，当天没执行第二天会接着执行，直到执行总次数=repeatCount+1次后停止执行
        // endingDailyAfterCount(repeatCount) 每天执行repeatCount+1次后结束
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("total", 10);
        TriggerKey triggerKey = TriggerKey.triggerKey("t22r1", "s22nv");
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobDetail)
//                .startNow()
                .withSchedule(DailyTimeIntervalScheduleBuilder
                        .dailyTimeIntervalSchedule()
                        .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(start, 53))
                        .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(end, 58))
                        .withIntervalInSeconds(intervalInSeconds)
//                        .endingDailyAfterCount(repeatCount)
//                        .withRepeatCount(repeatCount)
                        .onEveryDay())
                .usingJobData(jobDataMap)
                .build();

        if(!scheduler.checkExists(jobDetail.getKey())) {
            scheduler.addJob(jobDetail, false);
        }

        scheduler.scheduleJob(trigger);

        //第二次请求

        Thread.sleep(3000);

        jobDetail = SingleJobDetailFactory.getJobDetail(JobDetailIdentity.HELLO_JOB_TEST1);
        jobDataMap = new JobDataMap();
        jobDataMap.put("total", 20);
        TriggerKey triggerKey1 = TriggerKey.triggerKey("tr1", "snv");
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey1)
                .forJob(jobDetail)
//                .startNow()
                .withSchedule(DailyTimeIntervalScheduleBuilder
                        .dailyTimeIntervalSchedule()
                        .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(start, 53))
                        .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(end, 58))
                        .withIntervalInSeconds(intervalInSeconds)
//                        .endingDailyAfterCount(repeatCount)
//                        .withRepeatCount(repeatCount)
                        .onEveryDay())
                .usingJobData(jobDataMap)
                .build();


        if(!scheduler.checkExists(jobDetail.getKey())) {
            scheduler.addJob(jobDetail, false);
        }

        scheduler.scheduleJob(trigger1);



        // 启动调度器 启动和调度器关联数序可以调换
        scheduler.start();

    }

}
