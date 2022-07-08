//package com.ehang.mysql.mybatis.plus.generator.cls.service;
//
//import com.jayway.jsonpath.JsonPath;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) throws Exception {
//        URL url = new URL("https://www.douyin.com/web/api/v2/aweme/iteminfo/?item_ids=7069689032921386247");
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setRequestMethod("GET");
//        httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Mobile Safari/537.36");
//
//        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
//                ? httpConn.getInputStream()
//                : httpConn.getErrorStream();
//        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
//        String response = s.hasNext() ? s.next() : "";
//        System.out.println(response);
//
//        String vid = JsonPath.parse(response).read("$.item_list[0].video.vid", String.class);
//        System.out.println(vid);
//
//        String realVideoUrl = "https://aweme.snssdk.com/aweme/v1/play/?video_id=" + vid + "&ratio=720p&line=0";
//        System.out.println("无水印地址：" + realVideoUrl);
//
//        String duration = JsonPath.parse(response).read("$.item_list[0].duration", String.class);
//        System.out.println("视频时长：" + duration);
//
//        String originCoverUrl = JsonPath.parse(response).read("$.item_list[0].video.origin_cover.url_list[0]", String.class);
//        System.out.println("封面地址：" + originCoverUrl);
//
//        String desc = JsonPath.parse(response).read("$.item_list[0].desc", String.class);
//        System.out.println("文案：" + desc);
//
//        String musicTitle = JsonPath.parse(response).read("$.item_list[0].music.title", String.class);
//        System.out.println("音乐名称：" + musicTitle);
//        String musicAuthor = JsonPath.parse(response).read("$.item_list[0].music.author", String.class);
//        System.out.println("音乐作者：" + musicAuthor);
//        String musicUrl = JsonPath.parse(response).read("$.item_list[0].music.play_url.uri", String.class);
//        System.out.println("音乐下载地址：" + musicUrl);
//
//        String diggCount = JsonPath.parse(response).read("$.item_list[0].statistics.digg_count", String.class);
//        System.out.println("爱心数量：" + diggCount);
//        String commentCount = JsonPath.parse(response).read("$.item_list[0].statistics.comment_count", String.class);
//        System.out.println("评论数量：" + commentCount);
//        String shareCount = JsonPath.parse(response).read("$.item_list[0].statistics.share_count", String.class);
//        System.out.println("分享数量：" + shareCount);
//
//    }
//}
