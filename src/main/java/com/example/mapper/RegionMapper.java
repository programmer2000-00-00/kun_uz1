package com.example.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionMapper {
    private Integer id;
    private String key;
    private String name;

    public RegionMapper(Integer id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }
}


