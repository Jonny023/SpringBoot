package com.example.springbootquartz.scheduled;

import com.example.springbootquartz.service.TestService;
import com.example.springbootquartz.utils.ApplicationContextUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 测试
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestScheduled extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(TestScheduled.class);

    private static TestService testService;

    static {
        TestScheduled.testService = ApplicationContextUtil.getBean(TestService.class);
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
