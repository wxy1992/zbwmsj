package com.wmsj.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoConvertUtil {
    //定义一个输入流
    private static String inputPath = "";
    //定义一个输出流
    private static String outputPath = "";
    //FFmpeg 文件位置
    private static String ffmpegPath = "";

    public static void execute(){
        getPath();

        if (!checkfile(inputPath)) {
            System.out.println(inputPath + " is not file");
            return;
        }
        if (process()) {
            System.out.println("ok");
        }
    }
    /**
     * 获取Path 路径
     */
    public static void getPath() {
        // 先获取当前项目路径，在获得源文件、目标文件、转换器的路径
        File diretory = new File("");
        try {
            String currPath = diretory.getAbsolutePath();
            inputPath = "D:\\attachment\\22.flv";
            outputPath = "D:\\attachment\\22.mp4";
            ffmpegPath = "E:\\ffmpeg-20200420\\bin\\";
            System.out.println(currPath);
        } catch (Exception e) {
            System.out.println("获取文件路径出错 ");
        }
    }
    public static boolean process() {
        //0:FFmpeg  1:mencoder  9:无对应类型
        int type = checkContentType();
        System.out.println(type);
        boolean status = false;
        //FFmpeg能解析格式  直接转MP4
        if (type == 0) {
            System.out.println("直接转成mp4格式");
            //记录开始时间
            long startTime = System.currentTimeMillis();
            status = processMp4(inputPath);
            //记录结束时间
            long endTime = System.currentTimeMillis();
            //执行时长
            float excTime = (float) (endTime - startTime) / 1000;
            System.out.println("执行时长" + excTime + "s");
            return status;
        }
        // FFmpeg无法解析格式，使用mencoder转换AVI后再FFmpeg转MP4
        if (type == 1) {
            System.out.println("mencoder转换AVI");
            //记录开始时间
            long startTime = System.currentTimeMillis();
            String avifilepath = processAVI(type);
            if (avifilepath == null) {
                System.out.println("avi文件没有得到");
                return false;
            } else {
                status = processMp4(inputPath);
            }
            //记录结束时间
            long endTime = System.currentTimeMillis();
            //执行时长
            float excTime = (float) (endTime - startTime) / 1000;
            System.out.println("执行时长" + excTime + "s");
            return status;
        }
        if (type == 9) {
            System.out.println("格式非法！");
            return false;
        }
        return status;
    }

    /**
     * 判断FFmpeg能否转换  0:FFmpeg  1:mencoder  9:无法转换
     * @return
     */
    private static int checkContentType() {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length()).toLowerCase();
        // ffmpeg能解析的格式：（avi mpg wmv 3gp mov mp4 asf asx flv wmv9 rm rmvb ）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        /**
         * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),先用工具（mencoder）转换为avi(ffmpeg能解析的)格式.
         */
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }
    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式
     * @param type
     * @return
     */
    private static String processAVI(int type) {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegPath + "mencoder");
        commend.add(inputPath);
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("mp4");
        commend.add("-o");
        commend.add(outputPath + "a.AVI");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(commend).redirectErrorStream(true).start();
//            new PrintStream(process.getInputStream());
//            new PrintStream(process.getErrorStream());
            process.waitFor();
            return outputPath + "a.AVI";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean processMp4(String oldfilepath) {
        if (!checkfile(inputPath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }
        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath + "ffmpeg");
        command.add("-i");
        command.add(oldfilepath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-mbd");
        command.add("0");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-movflags");
        command.add("faststart");
        command.add(outputPath + "a.mp4");
        try {

            // 方案1
            //        Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " + oldfilepath
            //                + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
            //                + outputPath + "a.flv");

            // 方案2
            Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

//            new PrintStream(videoProcess.getErrorStream()).start();

//            new PrintStream(videoProcess.getInputStream()).start();

            videoProcess.waitFor();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }
}
