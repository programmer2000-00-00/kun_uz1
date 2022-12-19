package com.example.dto.article;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.CatagoryDTO;
import com.example.dto.RegionDTO;
import com.example.dto.profile.ProfileDTO;
import com.example.entity.AttachEntity;
import com.example.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleFilterDTO {
    private String id;
    private String title;

    private Integer regionId;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate fromPublishedDate;
    private LocalDate toPublishedDate;
    private Boolean visible;

}
