package com.example.o2o.util;

import com.example.o2o.constants.ConfigureConstant;
import com.example.o2o.constants.PathConstant;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.util
 * @date:2019/7/25
 **/
public class PathUtil {
    //返回项目图片相对路径
    private static String  separator = System.getProperty("file.separator");
    //得到基础路径
    public static String getImageBasePath(){
        String os = System.getProperty("os.name");
        String basePath;
        if (os.toLowerCase().startsWith("win")){
            basePath = PathConstant.winLocalImagePath;
        }else{
            basePath = PathConstant.linuxLocalImagePath;
        }
        basePath = basePath.replace("/",separator);
       return  basePath;

    }

    /**
     * 得到商铺路径的地址
     * @param shopId
     * @return
     */
    public static String getShopImagePath(long shopId){
        String imagePath = "/upload/item/shop"+shopId+"/";
        return imagePath.replace("/",separator);
    }
}
