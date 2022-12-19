package com.example.mapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class ArticleMapper {
    private Integer id;
    private String key;
    private  String name;

    public ArticleMapper(Integer id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }
}
