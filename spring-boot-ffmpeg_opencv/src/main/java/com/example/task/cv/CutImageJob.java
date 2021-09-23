package com.example.task.cv;

import com.example.task.base.CutImageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//public abstract class CutImageJob extends QuartzJobBean {
public abstract class CutImageJob {

    private Logger LOG = LoggerFactory.getLogger(CutImageJob.class);

//    private RedisUtil redisUtil;
//    private TaskService taskService;
//
//    {
//        redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);
//        taskService = ApplicationContextUtil.getBean(TaskService.class);
//    }

//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) {
//        JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
//        CutDataDetail cutDataDetail = Convertor.map2Bean(jobDataMap, CutDataDetail::new);
//
//    }

    public abstract CutImageStatus doCut(CutDataDetail cutDataDetail);

}
