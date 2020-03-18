package com.example.o2o.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.o2o.constants.ConfigureConstant;
import com.example.o2o.constants.PathConstant;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.entity.Shop;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: sso文件上传的工具类
 * @date: 2020/1/16 21:47
 **/
public class FileUploadUtil {

    public static final String objectName = "upload/item/shop1/";


    /**
     * 根据文件流上传文件
     * @param imageHolder
     * @param imageHolder
     * @return
     */
    public static String uploadFile(ImageHolder imageHolder, Shop shop){
        try{
            String objectName = ConfigureConstant.ossObjectName+"/shop"+shop.getShopId()+"/"+ImageUtil.getRandomFileName()+
                    ImageUtil.getFileExtension(imageHolder.getImageName());
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(PathConstant.ossWriteImageUrl,
                    ConfigureConstant.ossAccessKeyId, ConfigureConstant.ossAccessKeySecret);
            ossClient.putObject(ConfigureConstant.ossBucketName, objectName, imageHolder.getImage());
            // 关闭OSSClient。
            ossClient.shutdown();
            return PathConstant.ossReadImageUrl+objectName;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("上传到oss出错："+e.getMessage());
        }
    }
}
