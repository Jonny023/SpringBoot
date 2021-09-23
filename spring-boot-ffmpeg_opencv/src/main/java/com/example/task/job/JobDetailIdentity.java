//package com.example.task.job;
//
//
//import com.example.task.cv.FFmpegRuntimeCutImageJob;
//
//public enum JobDetailIdentity {
//    FFMPEG("FFMPEG_CUT_IMAGE_NAME", "FFMPEG_CUT_IMAGE_GROUP", FFmpegRuntimeCutImageJob.class),
//    ,TASK_UPDATE_STATE("TASK_UPDATE_STATE_NAME","TASK_UPDATE_STATE_GROUP", UpdateTaskStateJob.class)
//    ;
//
//    JobDetailIdentity(String name, String group, Class<? extends Job> clazz) {
//        this.name = name;
//        this.group = group;
//        this.clazz = clazz;
//    }
//
//    private String name;
//
//    private String group;
//
//    private Class<? extends Job> clazz;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//
//    public Class<? extends Job> getClazz() {
//        return clazz;
//    }
//
//    public void setClazz(Class<? extends Job> clazz) {
//        this.clazz = clazz;
//    }
//
//    public String getIdentity() {
//        return name+group;
//    }
//}
