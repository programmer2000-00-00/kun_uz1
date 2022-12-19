package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name="catagory")
public class CatagoryEntity {
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
    private String name_eng;
    @Column
    private Boolean visible;
    @Column
    private LocalDate localDate;
    @Column
    private Integer pId;

}
