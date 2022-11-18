package com.example.javacv.converter;

import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H265;

/**
 * ffmpeg框架转换器
 */
public class FFmpegFrameConverter extends AbstractThreadPoolConverter{

    protected final Logger logger = LoggerFactory.getLogger(FFmpegFrameConverter.class);

    protected String src;
    protected String out;

    public FFmpegFrameConverter(String src, String out, ThreadPoolExecutor executor) {
        super(executor);
        this.src = src;
        this.out = out;
    }

    @Override
    protected void convert() {
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;
        try {
            grabber = initFrameGrabber(src);
            recorder = initFrameRecorder(grabber, out);
            push(grabber,recorder);
        } catch (Exception e) {
            logger.error("推流异常:{}", e);
        } finally {
            logger.info("推流结束:"+src);
            closeResource(grabber, recorder);
        }
    }

    /**
     * 初始化拉流器
     * @param src
     * @return
     */
    protected FFmpegFrameGrabber initFrameGrabber(String src) {
        Map<String, String> options = new HashMap();
        options.put("fflags", "nobuffer");
        options.put("stimeout", "5000000");
        if(src.startsWith("rtsp")) {
            options.put("rtsp_transport","tcp");
        }

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(src);
        grabber.setOptions(options);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            logger.error("初始化拉流器异常:{}", e);
            return null;
        }
        // 若视频像素值为0，说明拉流异常，程序结束
        /*if (grabber.getImageWidth() == 0 && grabber.getImageHeight() == 0) {
            logger.error(src + "  拉流异常");
            close(grabber, null);
            return null;
        }*/
        return grabber;
    }

    /**
     * 初始化推流器
     * @param grabber
     * @param out
     * @return
     */
    protected FFmpegFrameRecorder initFrameRecorder(FFmpegFrameGrabber grabber, String out) {
        boolean isH265 = grabber.getVideoCodec()==AV_CODEC_ID_H265;
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(out, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setVideoOption("crf", "28");// 画面质量参数，0~51；18~28是一个合理范围
        recorder.setGopSize(2);
        recorder.setFrameRate(grabber.getVideoFrameRate());
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setAudioChannels(grabber.getAudioChannels());
        recorder.setAudioBitrate(grabber.getAudioBitrate()<1?128*1000:grabber.getAudioBitrate());
        recorder.setSampleRate(grabber.getSampleRate());
        AVFormatContext fc = null;
        if (out.startsWith("rtmp") || out.startsWith("flv")) {
            // 封装格式flv
            recorder.setFormat("flv");
            recorder.setAudioCodecName("aac");
            recorder.setVideoCodec(AV_CODEC_ID_H264);
            if(!isH265) {
                fc = grabber.getFormatContext();
            }
        }
        try {
            if(isH265) {
                recorder.start();
            } else {
                recorder.start(fc);
            }
        } catch (FrameRecorder.Exception e) {
            logger.error("初始化推流器异常:{}", e);
            return null;
        }
        return recorder;
    }

    /**
     * 推流
     * @param grabber
     * @param recorder
     */
    protected void push(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) {
        if(grabber.getVideoCodec() == AV_CODEC_ID_H265) {
            push265(grabber, recorder);
        } else {
            push264(grabber, recorder);
        }
    }

    private void push264(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) {
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        int err_index = 0;//采集或推流导致的错误次数
        int no_frame_index=0;//没有采集到帧的次数
        //连续5次没有采集到帧则认为视频采集结束，程序错误次数超过10次即中断程序
        while (no_frame_index < 5 && err_index < 1) {
            if(isInterrupted()) {
                //取消推流
                break;
            }
            AVPacket pkt=null;
            try {
                //没有解码的音视频帧
                pkt=grabber.grabPacket();
                if(pkt==null||pkt.size()<=0||pkt.data()==null) {
                    //空包记录次数跳过
                    no_frame_index++;
                    continue;
                }
                //不需要编码直接把音视频帧推出去
                err_index+=(recorder.recordPacket(pkt)?0:1);//如果失败err_index自增1
                //av_packet_unref(pkt);
            } catch (Exception e) {//推流失败
                err_index++;
                logger.error("推流失败: {}", e);
            }
        }
    }

    private void push265(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) {
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            IplImage iplImage = null;
            Frame frame = null;
            while((iplImage = converter.convert(grabber.grab())) != null) {
                if(isInterrupted()) {
                    //取消推流
                    break;
                }
                frame = converter.convert(iplImage);
                recorder.record(frame, grabber.getPixelFormat());
            }
        } catch (FrameGrabber.Exception|FrameRecorder.Exception e) {
            logger.error("推流失败: {}", e);
        } catch (Exception e) {
            logger.error("推流失败: {}", e);
        }
    }

    private void closeResource(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) {
        try {
            if (grabber != null) {
                grabber.close();
            }
            if (recorder != null) {
                recorder.close();
            }
        } catch (FrameRecorder.Exception e) {
            logger.error("关闭grabber异常:{}", e);
        } catch (FrameGrabber.Exception e) {
            logger.error("关闭recorder异常:{}", e);
        }
    }
}
