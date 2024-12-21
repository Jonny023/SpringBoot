package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.example.domain.vo.BasePage;
import com.example.domain.vo.PageVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author admin
 * @description 分页工具
 */
@Service
public class PageService {

    /**
     * 限制大小
     */
    private static final int LIMIT_SIZE = 5000;

    /**
     * 静态线程池，用于复用
     */
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            10,
            20,
            20,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10000),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 获取分页方法所有数据
     *
     * @param param        查询参数
     * @param pageFunction 业务分页方法函数
     * @param <T>          结果类型
     * @param <Q>          参数类型
     * @return 分页所有数据
     */
    public <T, Q extends BasePage> List<T> fetchAll(Q param, Function<Q, PageVO<T>> pageFunction) {
        param.setPageSize(LIMIT_SIZE);
        List<T> allData = Lists.newArrayList();
        PageVO<T> pageVO;
        int page = 1;
        do {
            param.setPage(page);
            pageVO = pageFunction.apply(param);
            Optional.ofNullable(pageVO).map(PageVO::getData).filter(CollUtil::isNotEmpty).ifPresent(allData::addAll);
            page++;
        } while (Objects.nonNull(pageVO) && CollUtil.isNotEmpty(pageVO.getData()) && pageVO.getData().size() == LIMIT_SIZE);
        return allData;
    }

    /**
     * 异步获取分页方法所有数据【无序】(未验证)
     *
     * @param param        查询参数
     * @param pageFunction 业务分页方法函数
     * @param <T>          结果类型
     * @param <Q>          参数类型
     * @return 分页所有数据
     */
    @SuppressWarnings("all")
    public <T, Q extends BasePage> List<T> asyncFetchAll(Q param, Function<Q, PageVO<T>> pageFunction) {
        param.setPageSize(LIMIT_SIZE);
        List<T> allData = Lists.newArrayList();

        AtomicInteger currentPage = new AtomicInteger(1);
        List<CompletableFuture<Void>> futures = Lists.newArrayList();

        while (true) {

            int page = currentPage.getAndIncrement();
            Q paramCopy = (Q) BeanUtil.copyProperties(param, param.getClass());
            paramCopy.setPage(page);

            // 异步执行分页查询
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                PageVO<T> pageVO = pageFunction.apply(paramCopy);
                Optional.ofNullable(pageVO)
                        .map(PageVO::getData)
                        .filter(CollUtil::isNotEmpty)
                        .ifPresent(allData::addAll);

                // 如果当前页的数据小于 LIMIT_SIZE，说明已经查询完所有数据，可以停止分页
                if (pageVO.getData().size() < LIMIT_SIZE) {
                    currentPage.set(Integer.MAX_VALUE);
                }
            }, EXECUTOR);
            futures.add(future);

            // 如果 currentPage 已经设置为最大值，表示查询已完成
            if (currentPage.get() == Integer.MAX_VALUE) {
                break;
            }
        }

        // 等待所有异步任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return allData;
    }

    /**
     * 异步获取分页方法所有数据【有序】(未验证)
     *
     * @param param        查询参数
     * @param pageFunction 业务分页方法函数
     * @param <T>          结果类型
     * @param <Q>          参数类型
     * @return 分页所有数据
     */
    @SuppressWarnings("all")
    public <T, Q extends BasePage> List<T> asyncFetchAllSort(Q param, Function<Q, PageVO<T>> pageFunction) {
        param.setPageSize(LIMIT_SIZE);
        List<T> allData = Lists.newArrayList();

        AtomicInteger currentPage = new AtomicInteger(1);
        List<CompletableFuture<PageVO<T>>> futures = Lists.newArrayList();

        while (true) {
            int page = currentPage.getAndIncrement();
            Q paramCopy = (Q) BeanUtil.copyProperties(param, param.getClass());
            paramCopy.setPage(page);

            // 异步执行分页查询
            CompletableFuture<PageVO<T>> future = CompletableFuture.supplyAsync(() -> pageFunction.apply(paramCopy), EXECUTOR);
            futures.add(future);

            // 结束条件：数据小于LIMIT_SIZE，说明查询完毕
            PageVO<T> pageVO = future.join();
            if (pageVO.getData().size() < LIMIT_SIZE) {
                break;
            }
        }

        // 等待所有异步任务完成并按页码顺序合并结果
        List<PageVO<T>> pageVOs = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 合并所有结果
        pageVOs.stream()
                .map(PageVO::getData)
                .filter(CollUtil::isNotEmpty)
                .forEach(allData::addAll);

        return allData;
    }

}