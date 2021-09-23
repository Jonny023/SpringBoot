package com.example.javacv.converter;

public class FFmpegRuntimeConverter extends AbstractRuntimeConverter{

    protected String src;
    protected String out;

    public FFmpegRuntimeConverter(String src, String out) {
        this.src = src;
        this.out = out;
    }

    @Override
    protected String cmd() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")){
            return "cmd /c start ffmpeg -i " + src + " -vcodec copy -acodec copy -f flv " + out;
        }
        return "ffmpeg -rtsp_transport tcp -i " + src + " -c:v libx264 -acodec copy -f flv " + out;
//        return "ffmpeg -i " + src + " -vcodec copy -acodec copy -f flv " + out;
    }

    @Override
    protected void result(String result) {
        System.out.println(result);
    }
}
