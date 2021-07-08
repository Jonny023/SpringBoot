package com.example.springbootquartz.lisenter;

import com.example.springbootquartz.scheduled.TestScheduled;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SchedulerListener.class);

    private static String syncByHoursCron = "0 0/1 * * * ?";

    @Resource
    Scheduler scheduler;
    private static String TRIGGER_GROUP_NAME = "sync_trigger";
    private static String JOB_GROUP_NAME = "sync_job";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{

            TriggerKey triggerKeyByHours = TriggerKey.triggerKey("syncByHoursTrigger", TRIGGER_GROUP_NAME);
            Trigger triggerByHours = scheduler.getTrigger(triggerKeyByHours);
            if (triggerByHours == null) {
                triggerByHours = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKeyByHours)
                        .withSchedule(CronScheduleBuilder.cronSchedule(syncByHoursCron).withMisfireHandlingInstructionIgnoreMisfires())
                        .startNow()
                        .build();
                JobDetail jobDetail = JobBuilder.newJob(TestScheduled.class)
                        .withIdentity("syncByHoursJob", JOB_GROUP_NAME)
                        .requestRecovery(true)
                        .build();
                scheduler.scheduleJob(jobDetail, triggerByHours);
            }

            scheduler.start();
        }catch (SchedulerException e){
            log.error("sync job fail:{}",e);
        }
    }

}