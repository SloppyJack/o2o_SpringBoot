package com.example.o2o.util;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.util
 * @date:2019/9/3
 **/
@SuppressWarnings("Duplicates")
public class NetUtil {

    /**
     * 发送https请求
     *
     * @param requestUrl
     *            请求地址
     * @param requestMethod
     *            请求方式（GET、POST）
     *            提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod) {
        JSONObject jsonObj = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            //流转为字符串
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // 实例化输入流，并获取网页代码
            String temp; // 依次循环，至到读的值为空
            StringBuilder sb = new StringBuilder();
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
            }
            String content = sb.toString();
            System.out.println(content);
            jsonObj = (JSONObject)(new JSONParser().parse(content));
            inputStream.close();
            conn.disconnect();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    //返回字符串的方法
    public static String httpsRequest1(String requestUrl, String requestMethod,
                                       String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        /*JSONObject object = httpsRequest("https://api.imjad.cn/cloudmusic/?type=detail&id=1386080627","GET");
        //歌手名
        String song = ((Map)((JSONArray) object.get("songs")).get(0)).get("name").toString();
        String name = ((Map)((JSONArray)(((Map)((JSONArray) object.get("songs")).get(0)).get("ar"))).get(0)).get("name").toString();
        System.out.println(name);*/
        String s = "1386080627-128-8b4bafe794a4e4f844bb714ca10c7900";
        s.substring(0,s.indexOf("-"));
        System.out.println(s.substring(0,s.indexOf("-")));
    }
}
