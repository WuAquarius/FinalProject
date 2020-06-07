package com.example.finalproject.util;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpURLConn {
    // 项目服务器的地址
    //这里的ip地址，设置为自己网络的ip地址，这里注意，eclisp也就是电脑上面的网络应该和手机连接的网处于同一个网络
    // JSPStudy是自己eclisp上面的学生信息管理的java项目
    public static String BASE_URL = "http://192.168.0.107:8080/FinalProject2_war_exploded";

    /**
     * urlStr:网址
     * parms：提交数据
     * return:网页源码
     * */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getContextByHttp(String urlStr, Map<String, String> parms) {
        // 用于单线程字符串的拼接
        StringBuilder result = new StringBuilder();

        try {
            // 访问的资源路径
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求类型
            conn.setRequestMethod("POST");
            // 设置读写超时时间
            conn.setReadTimeout(5000);
            // 设置连接超时时间
            conn.setConnectTimeout(5000);
            // 设置url可用于输入
            conn.setDoInput(true);
            // 设置url可用于输出
            conn.setDoOutput(true);
            // 设置重定向，进行页面跳转
            conn.setInstanceFollowRedirects(true);
            // 获取HTTP输出流
            OutputStream out = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            // 缓冲输出流
            BufferedWriter writer = new BufferedWriter(osw);
            writer.write(getStringFormOutput(parms));

            // 控制台输出查看参数
            System.out.println("1" + getStringFormOutput(parms));
            System.out.println("2" + parms);

            // 关闭流
            writer.flush();
            writer.close();
            osw.close();
            out.close();

            // 获取HTTP返回码
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                // 获取HTTP输入流
                System.out.println("======================获取HTTP输入流================");
                InputStream in = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                    System.out.println(reader.readLine());
                }
            }else {
                return "error:0";
            }

            // 断开HTTP连接
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("result的结果" + result.toString());
        return result.toString();
    }

    private static String getStringFormOutput(Map<String,String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (isFirst){
                isFirst = false;
            }else {
                sb.append("&");
            }
            // 使用URLEncode做encode转码操作，
            sb.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        // 将sb转化为字符串
        return sb.toString();
    }
}
