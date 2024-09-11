package org.example.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Jonny
 * @description: 通用异步导出服务
 */
@Slf4j
public class ExportAsyncService<T> {

    private static final String EXCEL_FILE_SUFFIX = ".xlsx";
    private static final String ZIP_FILE_NAME = "%s.zip";
    private static final String TMP_DIR = "./csv/tmp/";
    private static final String ZIP_DIR = "./csv/zip/";

    /**
     * 读线程
     */
    private static final ExecutorService queryExecutor = new ThreadPoolExecutor(20, 50, 20L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000));
    /**
     * 用于控制线程数量
     */
    private CountDownLatch countDownLatch;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 导出文件名
     */
    private String exportFileName;
    /**
     * 时间戳（用于区分不同请求，防止数据错乱）
     */
    private String timestamp;

    /**
     * 初始化参数
     *
     * @param countDownLatch 用于控制线程数量
     * @param pages          总页数
     * @param exportFileName 导出文件名
     */
    public ExportAsyncService(CountDownLatch countDownLatch, int pages, String exportFileName) {
        this.pages = pages;
        this.countDownLatch = countDownLatch;
        this.exportFileName = exportFileName;
        this.timestamp = String.valueOf(Instant.now().toEpochMilli());
        createDirectories();
    }

    /**
     * 异步导出
     *
     * @param pageQueryFunction 分页查询函数
     * @param clazz             数据类型
     * @return CompletableFuture
     */
    public String asyncExport(Function<Integer, PageInfo<T>> pageQueryFunction, Class<T> clazz) {
        for (int i = 1; i <= pages; i++) {
            int currentPage = i;
            CompletableFuture.runAsync(() -> {
                try {
                    PageInfo<T> pageInfo = pageQueryFunction.apply(currentPage);
                    List<T> data = pageInfo.getList();
                    writeToExcel(data, currentPage, clazz);
                } catch (Exception e) {
                    log.error("Failed to export data", e);
                } finally {
                    countDownLatch.countDown();
                }
            }, queryExecutor);
        }

        try {
            countDownLatch.await(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String zipFilePath = createZipFile();
        return cleanupTempFiles(zipFilePath);
    }


    private void writeToExcel(List<T> data, int fileCounter, Class<T> dataClass) {
        if (CollUtil.isEmpty(data)) {
            return;
        }
        String fileName = Paths.get(TMP_DIR, timestamp, exportFileName + fileCounter + EXCEL_FILE_SUFFIX).toString();
        EasyExcel.write(fileName, dataClass)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("导出数据")
                .doWrite(data);
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(TMP_DIR, timestamp));
            Files.createDirectories(Paths.get(ZIP_DIR, timestamp));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }

    private String createZipFile() {
        String zipFilePath = Paths.get(ZIP_DIR, timestamp, String.format(ZIP_FILE_NAME, exportFileName)).toString();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.walk(Paths.get(TMP_DIR, timestamp))
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new CompletionException("Failed to create zip file", e);
        }
        log.info("Created zip file: {}", zipFilePath);
        return zipFilePath;
    }

    private String cleanupTempFiles(String zipFilePath) {
        try {
            Files.walk(Paths.get(TMP_DIR, timestamp))
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error("Failed to delete: {}", path);
                        }
                    });
        } catch (IOException e) {
            log.error("Error during cleanup: {}", e.getMessage());
        }
        return zipFilePath;
    }


}