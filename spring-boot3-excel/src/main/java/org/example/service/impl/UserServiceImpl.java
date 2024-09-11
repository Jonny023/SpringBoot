package org.example.service.impl;

import cn.hutool.core.date.StopWatch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.entity.SysUser;
import org.example.domain.request.UserPageReq;
import org.example.domain.vo.UserVO;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jonny
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String[] NAMES = {"刘德华", "陈龙", "吴彦祖", "甄子丹", "李连杰", "周星驰", "周润发", "梁朝伟", "张学友", "黎明"};
    private static final String[] ADDRESSES = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "成都", "重庆", "西安"};

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserVO> list() {
        return Stream.iterate(1, i -> i + 1).limit(200).map(i -> UserVO.builder()
                .id(Long.valueOf(i))
                .name(NAMES[new Random().nextInt(10)])
                .address(ADDRESSES[new Random().nextInt(10)])
                .build()).collect(Collectors.toList());
    }

    @Override
    public PageInfo<SysUser> page(UserPageReq req) {
        req.validate();
        // 使用 PageHelper 插件进行分页
        if (Boolean.TRUE.equals(req.getIsCount())) {
            long count = PageHelper.count(() -> userMapper.userList(req));
            PageInfo<SysUser> pageInfo = new PageInfo<>();
            pageInfo.setTotal(count);
            // 根据新的总记录数计算总页数
            int pageSize = req.getSize();
            long totalPage = (count + pageSize - 1) / pageSize;

            // 更新总页数
            pageInfo.setPages((int) totalPage);
            return pageInfo;
        } else {
            PageHelper.startPage(req.getCurrent(), req.getSize());
            // 紧接着的查询就是分页查询
            List<SysUser> users = userMapper.userList(req);
            // 使用 PageInfo 包装查询结果，这样便可以获取分页信息
            return new PageInfo<>(users);
        }
    }

    /**
     * 异步导出
     */
    @Async
    @Override
    public void exportUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int pageSize = 50000;

        try {
            UserPageReq countReq = new UserPageReq();
            countReq.setIsCount(true);
            countReq.setSize(pageSize);
            PageInfo<SysUser> countInfo = this.page(countReq);
            // 获取总页数
            int pages = countInfo.getPages();

            CountDownLatch countDownLatch = new CountDownLatch(pages);

            ExportAsyncService<SysUser> exportService = new ExportAsyncService<>(countDownLatch, pages, "用户信息");
            String path = exportService.asyncExport(
                    page -> {
                        // 分页查询
                        UserPageReq req = new UserPageReq();
                        req.setSize(pageSize);
                        req.setCurrent(page);
                        return this.page(req);
                    },
                    SysUser.class
            );

            log.info("Export completed. Zip file: {}", path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.info("共计耗时：{}秒", stopWatch.getTotalTimeSeconds());
    }

}
