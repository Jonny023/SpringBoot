//package com.example.service.impl;
//
//import com.example.javacv.ConvertStrategy;
//import com.example.javacv.ConverterServer;
//import com.example.javacv.exception.ConverterException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import sun.misc.BASE64Decoder;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class MediaServiceImpl implements MediaService {
//
//    private Logger log = LoggerFactory.getLogger(MediaServiceImpl.class);
//
//    private final static String HEAD = "MEDIA_";
//
//    @Value("${image.path}")
//    private String prefix;
//
//    @Value("${media.host}")
//    private String mediaPath;
//
//    @Resource
//    private RedisUtil redisUtil;
//
//    @Resource
//    private MediaMapper mediaMapper;
//
//    /**
//     * 直播推流
//     * @param cameraId
//     * @return
//     */
//    @Override
//    public String play(Long cameraId) {
//        String playAddress;
//        //尝试从redis中取得工作信息
//        Map map = (HashMap) redisUtil.get(HEAD + cameraId);
//        //若取不到，则新建任务并将任务信息存入redis
//        if (map == null) {
//            //统一定义变量
//            String src, out, workId;
//            String uuid = UUID.randomUUID().toString();
//            src = mediaMapper.queryStreamAddressByCameraId(cameraId);
//            if (src == null){
//                throw new BizException(ResultEnum.FFMPEG_START_ERROR);
//            }
//            out = "rtmp://" + mediaPath + "/hls/" + uuid;
//            try {
//                //开启推流得到workId
//                workId = ConverterServer.startConvert(src, out, ConvertStrategy.FFMEPG_FRAME);
//            }catch (ConverterException e){
//                throw new BizException(ResultEnum.FFMPEG_START_ERROR);
//            }
//            //得到播放地址并存入redis
//            playAddress = "http://" + mediaPath + ":8801/hls/" + uuid + ".m3u8";
//            Map cameraInfo = new HashMap();
//            cameraInfo.put("playAddress",playAddress);
//            redisUtil.set(HEAD + cameraId, cameraInfo,300);
//            redisUtil.set(HEAD + cameraId + "_WorkInfo",workId);
//        }
//        //若可取则直接返回播放地址
//        else {
//            playAddress = (String) map.get("playAddress");
//            redisUtil.expire(HEAD + cameraId,300);
//        }
//        return playAddress;
//    }
//
//    /**
//     * 关闭推流
//     * @param redisKey
//     */
//    @Override
//    public void shutdown(String redisKey) {
//        String workId = (String)redisUtil.get(redisKey + "_WorkInfo");
//        if (workId == null || workId.equals("")){
//            throw new BizException(ResultEnum.FFMPEG_STOP_ERROR);
//        }
//        redisUtil.del(redisKey + "_WorkInfo");
//        ConverterServer.closeConvert(workId);
//    }
//
////    /**
////     * 手动关闭重新推流
////     * @param cameraId
////     * @return
////     */
////    @Override
////    public String refresh(Long cameraId) {
////        Map workInfo = (HashMap)redisUtil.get(HEAD + cameraId);
////        if (workInfo == null){
////            return play(cameraId);
////        }
////        String workId = (String)workInfo.get("workId");
////        ConverterServer.closeConvert(workId);
////        return play(cameraId);
////    }
//
//    /**
//     * 保存截图
//     * @param screenshotRequestVO
//     * @return
//     */
//    @Override
//    public String screenshot(ScreenshotRequestVO screenshotRequestVO) {
//        String fileName = screenshotRequestVO.getPrefix() + "_" + screenshotRequestVO.getReceiveTime() + ".jpg";
//        String path = prefix + "/"  + screenshotRequestVO.getCameraId() + "/manual/";
//        //截取base64编码部分
//        screenshotRequestVO.setScreenshot(screenshotRequestVO.getScreenshot().split(",")[1]);
//        BASE64Decoder decoder = new BASE64Decoder();
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        try(OutputStream out = new FileOutputStream(path + fileName)) {
//            //解编码
//            byte[] b = decoder.decodeBuffer(screenshotRequestVO.getScreenshot());
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {// 调整异常数据
//                    b[i] += 256;
//                }
//            }
//            //写出文件
//            out.write(b);
//            out.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return path+ fileName;
//    }
//
//}
