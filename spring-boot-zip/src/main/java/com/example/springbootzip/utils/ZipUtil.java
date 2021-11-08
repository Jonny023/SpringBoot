package com.example.springbootzip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtil {

    private static Logger log = LoggerFactory.getLogger(ZipUtil.class);

    private ZipUtil() {
    }

    /**
     *  写入zip只能单线程写
     * @param response
     * @param files
     */
    public static void generateZip(HttpServletResponse response, List<File> files) {
        int size = files.size();
        AtomicInteger count = new AtomicInteger();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("打包下载.zip", "UTF-8"));
            CountDownLatch latch = new CountDownLatch(size);
            ThreadPoolUtil.execute(() -> {
                for (File file : files) {
                    log.info(Thread.currentThread().getName());
                    try {
                        byte[] resultByte = FileCopyUtils.copyToByteArray(file);
                        if (Objects.nonNull(resultByte)) {
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.write(resultByte);
                            count.incrementAndGet();
                        }
                    } catch (Exception e) {
                        log.info("下载zip失败：{}", e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
            latch.await(30, TimeUnit.SECONDS);
            zipOutputStream.finish();
            zipOutputStream.closeEntry();
        } catch (Exception e) {
            log.info("打包zip失败：{}", e);
        }
        log.info("总共请求记录:{}条,成功下载{}条", size, count.get());
        count.get();
    }
}
