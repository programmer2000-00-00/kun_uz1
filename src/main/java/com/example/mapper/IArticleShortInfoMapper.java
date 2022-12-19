package com.example.mapper;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public interface IArticleShortInfoMapper {
     String getId();
     String getTitle();
     String getDescription();
     String getImageId();
     LocalDateTime getPublishedDate();

}
