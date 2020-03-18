package com.example.o2o.util;

import com.example.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.util
 * @date:2019/7/25
 **/
public class ImageUtil {
    //获取ClassPath的绝对值路径
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    /**
     * 将commonsMultipartFile转换成File
     * @param commonsMultipartFile 文件类型
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile commonsMultipartFile){
        File file = new File(commonsMultipartFile.getOriginalFilename());
        try {
            commonsMultipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 处理缩略图，并返回新生成的相对值路径
     * @param imageHolder 图片持有者对象
     * @param targetAddr    目标地址
     * @return String
     * @throws IOException IO异常
     */
    public static String generateThumbnail(ImageHolder imageHolder, String targetAddr){
        String realFileName = getRandomFileName();
        String  extension = getFileExtension(imageHolder.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr+realFileName+extension;
        File dest = new File(PathUtil.getImageBasePath()+relativeAddr);
        try {
            Thumbnails.of(imageHolder.getImage()).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath+"/image/watermark.png")),1f)
                    .outputQuality(0.8f).toFile(dest);
        }catch (IOException e){
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        return relativeAddr;
    }

    public static String generateNormalImg(ImageHolder imageHolder, String targetAddr){
        String realFileName = getRandomFileName();
        String  extension = getFileExtension(imageHolder.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr+realFileName+extension;
        File dest = new File(PathUtil.getImageBasePath()+relativeAddr);
        //System.out.println("----------"+PathUtil.getImageBasePath()+relativeAddr);
        try {
            //System.out.println(basePath+"-----------");
            Thumbnails.of(imageHolder.getImage()).size(337,640)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath+"/image/watermark.png")),1f)
                    .outputQuality(0.9f).toFile(dest);
        }catch (IOException e){
            throw new RuntimeException("创建图片失败：" + e.toString());
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径涉及到的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImageBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     * @param fileName 文件名
     * @return
     */
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名
     * @return
     */
    public static String getRandomFileName() {
        //获取随机的五位数
        int ranNum = random.nextInt(89999)+10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr+ranNum;
    }

    /**
     * 判断storePath是文件路径还是目录路径，
     * 如果为文件路径删除该文件，
     * 如为目录路径则删除该目录下所有文件
     * @param storePath 文件路径或目录路径
     */
    public static void deleteFileOrPath(String storePath){
        //得到完整路径
        File fileOrPath = new File(PathUtil.getImageBasePath() + storePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                //列出所有文件
                File[] files= fileOrPath.listFiles();
                for (int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

    public static void main(String[] args) throws IOException {
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(basePath);
        Thumbnails.of(new File("C:\\Users\\Bin\\Desktop\\caixukun.jpg"))
                .size(400,400).watermark(Positions.BOTTOM_RIGHT,
                ImageIO.read(new File("C:\\Users\\Bin\\Desktop\\watermark.png")),1f)
                .outputQuality(0.8f).toFile("C:\\Users\\Bin\\Desktop\\new5.jpg");
    }

}
