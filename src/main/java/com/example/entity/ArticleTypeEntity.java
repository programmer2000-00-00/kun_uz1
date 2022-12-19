package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "article_type")
public class ArticleTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String key;
    @Column
    private String name_uz;
    @Column
    private String name_ru;
    @Column
    private String name_en;
    @Column
    private LocalDate createdDate;
    @Column
    private Integer prtId;
    @Column
    private Boolean visible=true;

    public ArticleTypeEntity() {
    }

}
