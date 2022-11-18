//package com.example.task.service.impl;
//
//import org.quartz.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Component
//public class TaskCommandLineRunner implements CommandLineRunner {
//
//    private Logger log = LoggerFactory.getLogger(TaskCommandLineRunner.class);
//
//    @Value("${schedules.task-update-state}")
//    private String cron;
//
//    @Resource
//    private Scheduler scheduler;
//
//    @Override
//    public void run(String... args) throws Exception {
//        try {
//            JobDetail jobDetail = SingleJobDetailFactory.getJobDetail(JobDetailIdentity.TASK_UPDATE_STATE);
//            TriggerKey triggerKey = TriggerKey.triggerKey(JobDetailIdentity.TASK_UPDATE_STATE.getName(),JobDetailIdentity.TASK_UPDATE_STATE.getGroup());
//            Trigger trigger = scheduler.getTrigger(triggerKey);
//            if (trigger == null) {
//                trigger = TriggerBuilder.newTrigger()
//                        .withIdentity(triggerKey)
//                        .withSchedule(CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionFireAndProceed())
//                        .startNow()
//                        .build();
//                scheduler.scheduleJob(jobDetail, trigger);
//            }
//            log.info("=========定时更新截图任务状态启动成功=========");
//        } catch (SchedulerException e) {
//            log.error("任务启动失败, {}", e);
//        }
//    }
//}
