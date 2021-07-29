package com.quartz.boot.job;

import com.quartz.boot.service.TestService;
import com.quartz.boot.utils.ApplicationContextUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 测试Job
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(TestJob.class);

    private static TestService testService;

    static {
        TestJob.testService = ApplicationContextUtil.getBean(TestService.class);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("------------------start------------------");
            testService.hello();
            log.info(context.getScheduler().getSchedulerInstanceId());
            log.info("------------------stop------------------");
        } catch (Exception e) {
            log.error("{}", e);
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            // 这个工作将立即重新开始
            jobExecutionException.setRefireImmediately(true);
            throw jobExecutionException;
        }
    }

}
