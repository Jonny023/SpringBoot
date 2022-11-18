package com.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 生成压缩文件
 */
public class ZipUtil {

    private static Logger log = LoggerFactory.getLogger(ZipUtil.class);

    private ZipUtil() {
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        createDirs(zipPath);
        try (
                FileOutputStream fos = new FileOutputStream(zipPath);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            // zos.setEncoding("gbk");//此处设置编码
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            log.error("{}", e);
        } catch (IOException e) {
            log.error("{}", e);
        }
    }

    /**
     *  创建目录
     * @param zipPath
     */
    private static void createDirs(String zipPath) {
        File fileDir = new File(zipPath);
        if (!fileDir.exists()) {
            fileDir.getParentFile().mkdirs();
        }
    }

    /**
     *  打包文件为zip，排除zip目录
     * @param file
     * @param parentPath
     * @param zos
     */
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory() && !parentPath.contains("zip") && !file.getPath().contains("zip")) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        log.error("{}", e);
                    }
                }
            } else if (!(file.getPath().contains("zip"))) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.error("{}", e);
                } catch (IOException e) {
                    log.error("{}", e);
                }
            }
        }
    }
}