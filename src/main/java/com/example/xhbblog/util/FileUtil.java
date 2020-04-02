package com.example.xhbblog.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//文件相关的配置类
public class FileUtil {
    public static String imgFileFolder()      //图片存储的公有路径
    {
        return "D:\\project\\xhb-blog\\src\\main\\resources\\static\\articleImg\\";
    }
    //将图片文件存入指定的默认路径中
    public static File imgToFileFolder(String imagePath,Integer id,UploadedImageFile uploadedImageFile) throws IOException {
        File imageFolder=new File(imgFileFolder()+imagePath);
        File file = new File(imageFolder,id+".jpg");
        if(!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return file;
    }

    public static File imgToFileFolder(String imagePath,String name,UploadedImageFile uploadedImageFile) throws IOException {
        File imageFolder=new File(imgFileFolder()+imagePath);
        File file = new File(imageFolder,name+".jpg");
        if(!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return file;
    }

    public static void delFile(File imageFolder,Integer id)        //文件删除
    {
        File file = new File(imageFolder,id+".jpg");
        if(file.exists())
        {
            file.delete();
            System.out.println(file+"已删除");
        }
    }

    //生成变形后存储的文件夹以及存储操作
    public static void imgResizeAndSave(File source,int width,int height,File target)
    {
        if(!target.getParentFile().exists())
        {
            target.getParentFile().mkdirs();          //第一次生成图片变形后的存储地址
        }
        ImageUtil.resizeImage(source, width,height, target);
    }
}
