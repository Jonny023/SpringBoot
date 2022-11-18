//package com.quartz.boot.lisenter;
//
//import com.quartz.boot.job.TestJob;
//import org.quartz.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//
//import javax.annotation.Resource;
//

/**
 * 用这种方式可能会出现启动多次，因为上下文变动就有影响
 */
////@Component
//public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    private static final Logger log = LoggerFactory.getLogger(SchedulerListener.class);
//
//    private static String CRON = "0 0/1 * * * ?";
//
//    @Resource
//    private Scheduler scheduler;
//    private static String TRIGGER_GROUP_NAME = "TestGroupTrigger";
//    private static String TRIGGER_GROUP = "TestGroupTrigger";
//    private static String GROUP_NAME = "GroupName";
//    public static final String JOB_NAME = "TestJob";
//    private static String JOB_GROUP_NAME = "TestJobGroup";
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        try {
//            TriggerKey testTriggerKey = TriggerKey.triggerKey(TRIGGER_GROUP_NAME, TRIGGER_GROUP);
//            Trigger trigger = scheduler.getTrigger(testTriggerKey);
//            if (trigger == null) {
//                trigger = TriggerBuilder.newTrigger()
//                        .withIdentity(testTriggerKey)
//                        .withSchedule(CronScheduleBuilder.cronSchedule(CRON).withMisfireHandlingInstructionIgnoreMisfires())
//                        .startNow()
//                        .build();
//                JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
//                        .withIdentity(JOB_NAME, JOB_GROUP_NAME)
//                        .requestRecovery(true)
//                        .build();
//                scheduler.scheduleJob(jobDetail, trigger);
//            }
//
//            //会自动start,可以不用手动start
//            //scheduler.start();
//        } catch (SchedulerException e) {
//            log.error("execute job failed:{}", e);
//        }
//    }
//
//}