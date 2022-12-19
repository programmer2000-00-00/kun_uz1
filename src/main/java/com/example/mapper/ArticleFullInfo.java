package com.example.mapper;

import lombok.Getter;
import lombok.Setter;

import javax.swing.plaf.synth.Region;

@Getter
@Setter
public class ArticleFullInfo {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private Region region;

}
