package com.example.task.cv;

import com.example.task.base.CutImageStatus;
import com.example.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class FFmpegRuntimeCutImageJob extends CutImageJob {

    private Logger LOG = LoggerFactory.getLogger(FFmpegRuntimeCutImageJob.class);

    @Override
    public CutImageStatus doCut(CutDataDetail cutDataDetail) {
        String cmd = "ffmpeg -i " + cutDataDetail.getSrc() + " -y -ss 0 -r 1 -vframes 1 -f image2 " + cutDataDetail.getFileDir() + File.separator
                + Instant.now().toEpochMilli() + "." + cutDataDetail.getImageType();
        LOG.info("{}", cmd);
        Runtime run = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = run.exec(cmd);
        } catch (IOException e) {
            LOG.error("{}", e);
        }
        try {
            int result = proc.waitFor();
            if (result == 0) {
                LOG.info("===================截图成功====================");
                return CutImageStatus.SUCCESS;
            } else {
                LOG.warn("截图失败: {}", JsonUtils.toJSONString(cutDataDetail));
                return CutImageStatus.FAIL;
            }
        } catch (IOException e) {
            LOG.error("截图失败: {}", e);
        } catch (InterruptedException e) {
            LOG.error("截图失败： {}", e);
        }
        return CutImageStatus.ERROR;
    }
}