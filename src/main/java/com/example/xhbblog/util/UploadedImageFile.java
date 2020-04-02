package com.example.xhbblog.util;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

public class UploadedImageFile {

    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
