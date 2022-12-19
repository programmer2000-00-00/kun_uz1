package com.example.dto;

import com.example.dto.article.ArticleDTO;
import com.example.dto.profile.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    @Positive
    private Integer profileId;
    @NotBlank
    private String content;
    @Positive
    private String articleId;
    @Positive
    private Integer replyId;
    private Boolean visible;
    @NotNull
    private ProfileDTO profile;
    @NotNull
    private ArticleDTO article;
    @NotNull
    private CommentDTO reply;

}
