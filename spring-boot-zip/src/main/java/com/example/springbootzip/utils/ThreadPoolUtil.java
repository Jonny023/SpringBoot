package com.example.springbootzip.utils;

import java.util.concurrent.*;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {

    /** 工具类，构造方法私有化 */
    private ThreadPoolUtil() {super();}

    // 线程池核心线程数
    private final static Integer CORE_POOL_SIZE = 3;
    // 最大线程数
    private final static Integer MAX_MUM_POOL_SIZE = 10;
    // 空闲线程存活时间
    private final static Integer KEEP_ALIVE_TIME = 3 * 60;
    // 线程等待队列
    private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
    // 线程池对象
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_MUM_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 向线程池提交一个任务,返回线程结果
     * @param r
     * @return
     */
    public static Future<?> submit(Callable<?> r) {
        return threadPool.submit(r);
    }

    /**
     * 向线程池提交一个任务，不关心处理结果
     * @param r
     */
    public static void execute(Runnable r) {
        threadPool.execute(r);
    }

    /** 获取当前线程池线程数量 */
    public static int getSize() {
        return threadPool.getPoolSize();
    }

    /** 获取当前活动的线程数量 */
    public static int getActiveCount() {
        return threadPool.getActiveCount();
    }
}