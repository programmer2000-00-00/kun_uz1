package com.example.repository;

import com.example.entity.EmailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;

public interface EmailRepositiry extends CrudRepository<EmailEntity,Integer>, PagingAndSortingRepository<EmailEntity,Integer> {
    List<EmailEntity> findByEmail(String email);


    List<EmailEntity> findByLocalDateTime(LocalDateTime parse);
}
