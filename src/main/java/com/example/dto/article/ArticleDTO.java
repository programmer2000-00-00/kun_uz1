package com.example.dto.article;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.CatagoryDTO;
import com.example.dto.RegionDTO;
import com.example.dto.profile.ProfileDTO;
import com.example.entity.AttachEntity;
import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String content;

    private Integer sharedCount;
    @Positive
    private String imageId;
    @NotNull
    private AttachEntity image;
    @NotNull
    private RegionDTO region;
    @Positive
    private Integer regionId;

    @Positive(message = "category must positive")
    private Integer catagoryId;
    @NotNull
    private CatagoryDTO catagory;
    @Positive
    private ProfileDTO moderatorId;
    @Positive
    private ProfileDTO publisherId;
    @NotNull
    private ArticleStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime publishedDate;

    private Boolean visible;

    private Integer viewCount=0;
    private ArticleTypeDTO articleType;
    private Integer articleTypeId;

}
