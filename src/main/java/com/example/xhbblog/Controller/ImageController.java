package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.PictureService;
import com.example.xhbblog.pojo.Picture;
import com.example.xhbblog.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    private QiniuUtil qiniuUtil;

    @Autowired
    private PictureService pictureService;         //用于将我所上传的图片存储到数据库中

    @RequestMapping("/uploadImage")          //通过七牛云进行文件上传
    public Map upload(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value= "editormd-image-file",  required = true) MultipartFile file) throws IOException {
        FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String fileExtend = originalFilename.substring(originalFilename.lastIndexOf("."));
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String fileKey = pictureService.getId().toString();
        String url=qiniuUtil.upload(fileInputStream,fileKey);     //保存在文件中,以图片数据库的自增长id进行保存

        Picture picture=new Picture();
        picture.setUrl("http://"+url);
        picture.setFilekey(fileKey);             //保存在数据库中
        pictureService.add(picture);


        Map anw=new HashMap();
        anw.put("url", "http://"+url);
        anw.put("success", 1);
        anw.put("message", "upload success!");
        return anw;
    }




}
