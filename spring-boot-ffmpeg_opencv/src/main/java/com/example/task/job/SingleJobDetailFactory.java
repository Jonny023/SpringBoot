package com.example.task.job;

//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingleJobDetailFactory {

//    private final static Map<String, JobDetail> jobDetailMap = new ConcurrentHashMap<>();
//
//    public static JobDetail getJobDetail(JobDetailIdentity jobDetailIdentity) {
//
//        if(jobDetailMap.containsKey(jobDetailIdentity.getIdentity())) {
//            return jobDetailMap.get(jobDetailIdentity.getIdentity());
//        }
//        synchronized (SingleJobDetailFactory.class) {
//            if(jobDetailMap.containsKey(jobDetailIdentity.getIdentity())) {
//                return jobDetailMap.get(jobDetailIdentity.getIdentity());
//            }
//            JobDetail jobDetail = JobBuilder.newJob(jobDetailIdentity.getClazz())
//                    .withIdentity(jobDetailIdentity.getName(), jobDetailIdentity.getGroup())
//                    .storeDurably()
//                    .build();
//            jobDetailMap.put(jobDetailIdentity.getIdentity(), jobDetail);
//            return jobDetail;
//        }
//
//    }
}

