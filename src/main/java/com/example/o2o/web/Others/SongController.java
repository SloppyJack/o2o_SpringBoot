package com.example.o2o.web.Others;

import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.Result;
import com.example.o2o.util.NetUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.Others
 * @date:2019/9/2
 **/
@Controller
@RequestMapping("/song")
public class SongController {

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/download",method = RequestMethod.POST)
    @ResponseBody
    private Result download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取前端传过来的文件流
        CommonsMultipartFile file;
        //CommonsMultipartResolver文件上传解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        //判断是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            file =(CommonsMultipartFile) multipartHttpServletRequest.getFile("song");
            //文件名
            String originalName = file.getOriginalFilename();
            //获得文件名中的Id参数
            String id =originalName.substring(0,originalName.indexOf("-"));
            //构建请求的url
            String url = "https://api.imjad.cn/cloudmusic/?type=detail&id="+id;
            //发起请求，获得JSONObject
            JSONObject jsonObject = NetUtil.httpsRequest(url,"GET");
            //歌曲名
            String songName = ((Map)((JSONArray) jsonObject.get("songs")).get(0)).get("name").toString();
            //歌手名
            String singerName = ((Map)((JSONArray)(((Map)((JSONArray) jsonObject.get("songs")).get(0)).get("ar"))).get(0)).get("name").toString();
            //文件名
            String fileName = songName+" - "+singerName+".mp3";
            System.out.println(songName);
            System.out.println(singerName);
            //将CommonsMultipartFile转为InputStream
            DiskFileItem fi = (DiskFileItem)file.getFileItem();
            InputStream inputStream = fi.getInputStream();
            download(request, response, inputStream,fileName);
            return Result.success();
        }else {
            return Result.error(CodeMsg.FAILED,"上传歌曲文件不能为空");
        }
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName){
        BufferedOutputStream bos = null;
        try {
            // 定义输出缓冲 10k
            byte[] buffer = new byte[10240];
            String userAgent = request.getHeader("user-agent").toLowerCase();
            System.out.println(userAgent);
            if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }
            fileName= fileName.replaceAll("\\+", "%20");
            System.out.println(fileName);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            bos = new BufferedOutputStream(response.getOutputStream());
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    buffer[i] ^= 0xa3;
                }
                bos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
