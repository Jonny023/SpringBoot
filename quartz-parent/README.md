# Quartz demo

[参考](https://juejin.cn/post/6946348432244080676)

## 要点
* 有状态
* 无状态
* 一个job可以对应多个trigger
* 一个trigger只能对应一个job
* starter会自动start，无需手动start

--

## 1.串行（阻塞）
```java
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestJob implements Job {
  
}

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestJob extends QuartzJobBean {
  
}
```

## 2.并行（非阻塞）
```java
public class TestJob implements Job {
  
}

public class TestJob extends QuartzJobBean {

}
```

---

# 每天定时任务

> 执行`count+1`次

* `endingDailyAfterCount(count)` 超过时间也会执行，如运行时段为8-10点，执行count次才结束
* `withRepeatCount(count)` 在时间范围内执行count次，未执行完下次继续执行
* 该api如果定义了次数，失败也会算为一次

```java
/**
 * 每天安时段执行
 */
public class EveryHourRangeJobTest {

    public static void main(String[] args) throws Exception {

        // 调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key", 0);
        JobKey jobKey = JobKey.jobKey("job1", "group1");
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .usingJobData(jobDataMap)
                .withIdentity(jobKey)
                .build();

        int repeatCount = 10; // 需执行的总次数
        int start = 11; // 几点开始
        int end = 11; // 几点结束
        int intervalInSeconds = 30; // 每隔n秒执行一次


        // withRepeatCount(repeatCount) 共执行repeatCount+1次，当天没执行第二天会接着执行，直到执行总次数=repeatCount+1次后停止执行
        // endingDailyAfterCount(repeatCount) 每天执行repeatCount+1次后结束
        TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
//                .startNow()
                .withSchedule(DailyTimeIntervalScheduleBuilder
                        .dailyTimeIntervalSchedule()
                        .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(start, 57))
                        .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(end, 58))
                        .withIntervalInSeconds(intervalInSeconds)
//                        .endingDailyAfterCount(repeatCount)
                        .withRepeatCount(repeatCount)
                        .onEveryDay())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        // 启动调度器 启动和调度器关联数序可以调换
        scheduler.start();

    }

}
```