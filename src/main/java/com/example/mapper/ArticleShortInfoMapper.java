package com.example.mapper;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class ArticleShortInfoMapper {
    String id;
    String title;
    String description;
    String imageId;
    LocalDate publishedDate;

    public ArticleShortInfoMapper(String id, String title, String description, String imageId, LocalDate publishedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
        this.publishedDate = publishedDate;
    }
}
