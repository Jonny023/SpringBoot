package com.quartz.boot.lisenter;

import com.quartz.boot.file.TimeMillisFileNameGenerator;
import com.quartz.boot.job.TestJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务器启动即开始执行
 */
@Component
public class RunJob implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SchedulerListener.class);

    private static String CRON = "0/5 * * * * ?";

    @Resource
    private Scheduler scheduler;
    private static String TRIGGER_GROUP_NAME = "TestGroupTrigger";
    private static String TRIGGER_GROUP = "TestGroupTrigger";
    private static String GROUP_NAME = "GroupName";
    public static final String JOB_NAME = "TestJob";
    private static String JOB_GROUP_NAME = "TestJobGroup";

    /**
     * 用starter会自动start，无需手动start
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        log.info("run command...");

        TriggerKey testTriggerKey = TriggerKey.triggerKey(TRIGGER_GROUP_NAME, TRIGGER_GROUP);
        // 重启和分布式都必须要获取和==null判断，因为job持久化到数据库，有唯一性，不然会报错
        Trigger trigger = scheduler.getTrigger(testTriggerKey);
        if (trigger == null) {
            /*trigger = TriggerBuilder.newTrigger()
                    .withIdentity(testTriggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(CRON).withMisfireHandlingInstructionIgnoreMisfires())
                    .startNow()
                    .build();*/
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(testTriggerKey)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                    .startNow()
                    .build();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("key", 0);
            jobDataMap.put("fileNameGenerator", new TimeMillisFileNameGenerator());
            JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                    .usingJobData(jobDataMap)
                    .withIdentity(JOB_NAME, JOB_GROUP_NAME)
                    .requestRecovery(true)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }

    }
}
