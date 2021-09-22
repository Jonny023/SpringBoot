package com.quartz.job;

import org.quartz.Job;

public enum JobDetailIdentity {
    HELLO_JOB_TEST1("n1", "g1", HelloJob.class),
    HELLO_JOB_TEST2("n2", "g1", HelloJob.class);

    JobDetailIdentity(String name, String group, Class<? extends Job> clazz) {
        this.name = name;
        this.group = group;
        this.clazz = clazz;
    }

    private String name;

    private String group;

    private Class<? extends Job> clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Class<? extends Job> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Job> clazz) {
        this.clazz = clazz;
    }

    public String getIdentity() {
        return name+group;
    }
}
