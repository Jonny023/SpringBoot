package com.example.task.cv;

import com.example.task.base.CutImageStatus;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 该方法逻辑暂时暂时不完善，主要是在total的判断上，改进后删除该条注释
 */
public class FFmpegFrameCutImageJob extends CutImageJob{

    private Logger LOG = LoggerFactory.getLogger(FFmpegFrameCutImageJob.class);

    @Override
    public CutImageStatus doCut(CutDataDetail cutDataDetail) {
        //初始化采集器
        Map<String, String> options = new HashMap();
        options.put("fflags", "nobuffer");
        options.put("stimeout", "5000000");
        if (cutDataDetail.getSrc().startsWith("rtsp")) {
            options.put("rtsp_transport", "tcp");
        }
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(cutDataDetail.getSrc());
        grabber.setOptions(options);
        try {
            grabber.start();

            //根据帧率计算采集间隔，秒
            double interval = grabber.getVideoFrameRate() * cutDataDetail.getIntervalSec();

            //frameRateCounter帧率计数器，totalCounter采集数计数器
            int frameRateCounter = 0, totalCounter = 0;
            while (true) {
                Frame frame = grabber.grabImage();
                if (++frameRateCounter >= interval) {
                    BufferedImage bufferedImage = Java2DFrameUtils.toBufferedImage(frame);
                    ImageIO.write(bufferedImage, cutDataDetail.getImageType(),
                            new File(cutDataDetail.getFileDir() + cutDataDetail.getFileNameGenerator().generator() + "." + cutDataDetail.getImageType()));
                    frameRateCounter = 0;
                    if (++totalCounter >= cutDataDetail.getTotal()) {
                        break;
                    }
                }
            }

            grabber.stop();
            return CutImageStatus.SUCCESS;
        } catch (FFmpegFrameGrabber.Exception e) {
            LOG.error("FFmpegFrameGrabber.Exception {}", e);
        } catch (IOException e) {
            LOG.error("IO error {}", e);
        }
        return CutImageStatus.ERROR;
    }
}
