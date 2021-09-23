//package com.example.task.cv;
//
//import com.geega.daas.bz.service.TaskService;
//import com.geega.daas.bz.util.ApplicationContextUtil;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
//public class UpdateTaskStateJob extends QuartzJobBean {
//
//    private static final Logger LOG = LoggerFactory.getLogger(UpdateTaskStateJob.class);
//
//    private static TaskService taskService;
//
//    static {
//        UpdateTaskStateJob.taskService = ApplicationContextUtil.getBean(TaskService.class);
//    }
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        LOG.info("============开始同步任务状态============");
//        taskService.updateTaskState();
//        LOG.info("============任务状态同步完成============");
//    }
//
//}