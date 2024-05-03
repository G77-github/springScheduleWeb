package com.example.EsaySchedule.dto;

import com.example.EsaySchedule.entity.Image;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageResponse {
    Long userId;
    String userName;
    Long imageId;
    String imageUrl;
    LocalDateTime imageDate;

    public ImageResponse(Image image, String userName) {

        this.userId = image.getUserId();
        this.userName = userName;
        this.imageId = image.getImgId();
        this.imageUrl = image.getImageUrl();
        this.imageDate = image.getImageDate();
    }
}
