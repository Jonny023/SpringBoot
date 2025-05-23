package com.example.springbootquartz.lisenter;

import com.example.springbootquartz.scheduled.TestScheduled;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Jonny
 * @description: 监听器，项目启动执行
 */
@Component
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SchedulerListener.class);
    private static final String EXEC_CRON = "0/10 * * * * ?";
    private static final String TRIGGER_KEY_NAME = "syncByHoursTrigger";
    private static final String TRIGGER_KEY_GROUP_NAME = "sync_trigger";
    private static final String JOB_NAME = "syncByHoursJob";
    private static final String JOB_GROUP_NAME = "sync_job";

    @Resource
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {

            // 避免多上下文重复执行
            if (event.getApplicationContext().getParent() != null) {
                return;
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_KEY_NAME, TRIGGER_KEY_GROUP_NAME);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobDetail jobDetail = JobBuilder.newJob(TestScheduled.class)
                        .withIdentity(JOB_NAME, JOB_GROUP_NAME)
                        // requestRecovery(true)类似断点续传，比如每小时执行一次，服务重启会继续执行错过的时间
                        .requestRecovery(true)
                        .build();
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(EXEC_CRON).withMisfireHandlingInstructionIgnoreMisfires())
                        .startNow()
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                // 检查任务状态并处理
                Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
                if (state == Trigger.TriggerState.PAUSED) {
                    scheduler.resumeTrigger(triggerKey);
                }
            }
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("sync job fail:", e);
        }
    }

}