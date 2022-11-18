package com.example.springbootrediscluster.consumer;

public interface RedisDelayQueueHandler<T> {

    void execute(T t);
}
