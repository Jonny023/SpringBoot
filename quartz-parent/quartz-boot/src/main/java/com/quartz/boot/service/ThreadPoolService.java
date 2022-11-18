package com.quartz.boot.service;

public class ThreadPoolService {

    /**
     * 自定义线程名称,方便的出错的时候溯源
     */
    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("my-thread-%d").build();

    /**
     * corePoolSize    线程池核心池的大小
     * maximumPoolSize 线程池中允许的最大线程数量
     * keepAliveTime   当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
     * unit            keepAliveTime 的时间单位
     * workQueue       用来储存等待执行任务的队列
     * threadFactory   创建线程的工厂类
     * handler         拒绝策略类,当线程池数量达到上线并且workQueue队列长度达到上限时就需要对到来的任务做拒绝处理
     */
    private static final ExecutorService SERVICE = new ThreadPoolExecutor(
            10,
            10,
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),
            NAMED_THREAD_FACTORY,
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    public static ExecutorService getEs() {
        return SERVICE;
    }

    /**
     * 使用线程池创建线程并异步执行任务
     *
     * @param task 任务
     */
    public static Future<?> newTask(Runnable task) {
        return SERVICE.submit(task);
    }

    public static Future<HashSet<String>> mongoTask(Callable<HashSet<String>> task) {
        return SERVICE.submit(task);
    }

}
