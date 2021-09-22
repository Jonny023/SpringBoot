package com.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            int len = children.length;
            //递归删除目录中的子目录下
            for (int i = 0; i < len; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     *  生成json文件到本地
     * @param path 存放路径
     * @param fileName 文件名，带后缀
     * @param data json字符
     */
    public static void generateJson(String path, String fileName, String data) {

        String outPath = path + fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outPath), "UTF-8")) {
            osw.write(data);
            osw.flush();//清空缓冲区，强制输出数据
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
