package com.example.o2o.dto;

import java.io.InputStream;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 图片持有者对象
 * @date:2019/10/8
 **/
public class ImageHolder {
    private String imageName;
    private InputStream image;

    public ImageHolder(){

    }

    /**
     * 图片的Holder
     * @param image 图片流
     * @param imageName 图片名
     */
    public ImageHolder(InputStream image,String imageName){
        this.image = image;
        this.imageName = imageName;
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }
}
