package com.example.entity;

import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="article")
public class ArticleEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String content;
    @Column
    private Integer sharedCount=0;
    @Column(name = "image_id")
    private String imageId;
    @JoinColumn(name = "image_id", updatable = false, insertable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity image;

    @Column(name = "region_id")
    private Integer regionId;
    @JoinColumn(name = "region_id", updatable = false, insertable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private RegionEntity region;

    @Column(name = "catagory_id")
    private Integer catagoryId;
    @JoinColumn(name = "catagory_id", updatable = false, insertable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private CatagoryEntity catagory;

    @Column(name = "moderator_id")
    private Integer moderatorId;
    @JoinColumn(name="moderator_id", updatable = false, insertable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @JoinColumn(name="publisher_id", updatable = false, insertable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private ProfileEntity publisher;

    @Enumerated(EnumType.STRING)
    @Column
    private ArticleStatus status;

    @Column
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column
    private LocalDateTime publishedDate;
    @Column
    private Boolean visible=Boolean.TRUE;
    @Column
    private Integer viewCount=0;

    @Column(name = "article_type_id")
    private Integer articleTypeId;
    @JoinColumn(name = "article_type_id", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleTypeEntity articleType;



}
