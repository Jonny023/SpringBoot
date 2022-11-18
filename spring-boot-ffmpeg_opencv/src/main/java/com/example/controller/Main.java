package com.example.controller;

import com.example.javacv.ConvertStrategy;
import com.example.javacv.ConverterServer;
import com.example.javacv.converter.FFmpegRuntimeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class Main {

    @Value("${media.host}")
    private String host;

    /**
     *  ffmpeg推流
     * @param source
     * @return
     */
    @RequestMapping("/ff_push")
    public String ffmpeg(String source) {
        String uuid = String.valueOf(Instant.now().toEpochMilli());
        String out = "rtmp://" + host + "/hls/" + uuid;
        ConverterServer.startConvert(source, out, ConvertStrategy.FFMPEG_RUNTIME);
        String m3u8 = "http://" + host + ":8801/hls/" + uuid + ".m3u8";
        return m3u8;
    }

    /**
     *  java推流
     * @param source
     * @return
     */
    @RequestMapping("/frame_push")
    public String frame(String source) {
        String uuid = String.valueOf(Instant.now().toEpochMilli());
        String out = "rtmp://" + host + "/hls/" + uuid;
        ConverterServer.startConvert(source, out, ConvertStrategy.FFMPEG_FRAME);
        String m3u8 = "http://" + host + ":8801/hls/" + uuid + ".m3u8";
        return m3u8;
    }
}
