package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column
    private LocalDateTime updateDate;

    @Column(name = "profile_id")
    private Integer profileId;
    @JoinColumn(name="profile_id",updatable = false,insertable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private ProfileEntity profile;

    @Column
    private String content;

    @Column(name="article_id")
    private String articleId;
    @JoinColumn(name="article_id",updatable = false,insertable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private ArticleEntity article;

    @Column(name = "reply_id")
    private Integer replyId;

    @JoinColumn(name="reply_id",updatable = false,insertable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private CommentEntity reply;

    private  Boolean visible;



}
