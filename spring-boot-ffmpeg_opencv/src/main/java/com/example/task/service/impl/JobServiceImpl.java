//package com.example.task.service.impl;
//
//import com.example.task.file.FileNameManualGenerator;
//import com.example.task.job.JobDetailIdentity;
//import com.example.task.job.SingleJobDetailFactory;
//import com.example.task.service.JobService;
//import org.joda.time.TimeOfDay;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.Trigger;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
//@Service
//public class JobServiceImpl implements JobService {
//
//    private Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class);
//
//    @Resource
//    private Scheduler scheduler;
//
//    @Override
//    public boolean add(Task task) {
//
//        try {
//
//            DateUtil.DataRange dataRange = DateUtil.string2Time(task.getCollectStart(), task.getCollectEnd());
//
//            int repeatCount = task.getCollectTotal(); // 需执行的总次数
//            int startHour = dataRange.getStartHour(); // 几点开始
//            int startMinute = dataRange.getStartMinute(); // 开始时间分钟数
//            int endHour = dataRange.getEndHour(); // 结束时间分钟数
//            int endMinute = dataRange.getEndMinute(); // 结束时间分钟数
//            int intervalInSeconds = task.getCollectRate().intValue(); // 每隔n秒执行一次
//
//            TriggerKey triggerKey = getTriggerKey(task);
//
//            JobDetail jobDetail = SingleJobDetailFactory.getJobDetail(JobDetailIdentity.FFMPEG);
//
//            JobDataMap jobDataMap = new JobDataMap();
//            jobDataMap.put(JobConst.TASK_ID.getKey(), task.getId());
//            jobDataMap.put(JobConst.TOTAL.getKey(), repeatCount);
//            jobDataMap.put(JobConst.INTERVAL_SEC.getKey(), intervalInSeconds);
//            jobDataMap.put(JobConst.SRC.getKey(), task.getStreamAddress());
//            jobDataMap.put(JobConst.IMAGE_TYPE.getKey(), JobConst.IMAGE_TYPE_PNG.getKey());
//            jobDataMap.put(JobConst.FILE_DIR.getKey(), String.format("%s/%s/%s", task.getFilePath(), task.getCameraId(), task.getId()));
//            jobDataMap.put(JobConst.FILE_NAME_GENERATOR.getKey(), new FileNameManualGenerator(task.getFilePrefix()));
//
//
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity(triggerKey)
//                    .forJob(jobDetail)
//                    .withSchedule(DailyTimeIntervalScheduleBuilder
//                            .dailyTimeIntervalSchedule()
//                            .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(startHour, startMinute))
//                            .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(endHour, endMinute))
//                            .withIntervalInSeconds(intervalInSeconds)
//                            .onEveryDay())
//                    .usingJobData(jobDataMap)
//                    .startNow()
//                    .build();
//
//            if (!scheduler.checkExists(jobDetail.getKey())) {
//                scheduler.addJob(jobDetail, false);
//            }
//
//            scheduler.scheduleJob(trigger);
//            LOG.info("=========任务启动成功=========");
//            return true;
//        } catch (SchedulerException e) {
//            LOG.error("任务启动失败, {}", e);
//        }
//
//        return false;
//
//    }
//
//    @Override
//    public boolean delete(Task task) {
//        try {
//            scheduler.unscheduleJob(getTriggerKey(task));
//            return true;
//        } catch (SchedulerException e) {
//            LOG.error("任务删除失败, {}", e);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean pause(Task task) {
//        try {
//            scheduler.pauseTrigger(getTriggerKey(task));
//            return true;
//        } catch (SchedulerException e) {
//            LOG.error("任务暂停失败, {}", e);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean resume(Task task) {
//        try {
//            scheduler.resumeTrigger(getTriggerKey(task));
//            return true;
//        } catch (SchedulerException e) {
//            LOG.error("任务恢复失败, {}", e);
//        }
//        return false;
//    }
//
//    /**
//     * 更新摄像头时级联更新任务中的视频流地址
//     *
//     * @param task
//     * @param streamAddress 视频流地址
//     */
//    @Override
//    public void updateTriggerDataJob(Task task, String streamAddress) {
//        TriggerKey triggerKey = getTriggerKey(task);
//        try {
//            Trigger trigger = scheduler.getTrigger(triggerKey);
//            if (trigger != null) {
//                Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
//                switch (triggerState) {
//                    case NORMAL:
//                    case ERROR:
//                    case BLOCKED:
//                    case PAUSED:
//                        if (!streamAddress.equals(trigger.getJobDataMap().getString(JobConst.SRC.getKey()))) {
//                            trigger.getJobDataMap().put(JobConst.SRC.getKey(), streamAddress);
//                            scheduler.rescheduleJob(triggerKey, trigger);
//                        }
//                        break;
//                }
//            }
//        } catch (SchedulerException e) {
//            LOG.error("获取任务trigger异常：{}", e);
//        }
//    }
//
//    private TriggerKey getTriggerKey(Task task) {
//        return TriggerKey.triggerKey(String.format("%s%s", JobConst.KEY_NAME.getKey(), task.getId()), String.format("%s%s", JobConst.KEY_GROUP.getKey(), task.getId()));
//    }
//
//}
