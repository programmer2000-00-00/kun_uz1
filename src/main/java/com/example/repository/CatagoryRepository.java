package com.example.repository;

import com.example.dto.CatagoryDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CatagoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.mapper.ArticleMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CatagoryRepository extends CrudRepository<CatagoryEntity,Integer>, PagingAndSortingRepository<CatagoryEntity,Integer> {

    @Transactional
    @Modifying
    @Query("update CatagoryEntity c set c.key=?1,c.name_uz=?2,c.name_ru=?3,c.name_eng=?4 where c.id=?5")
    Integer updateById(String key, String name_uz, String name_ru, String name_eng, Integer id);

    @Transactional
    @Modifying
    @Query("update CatagoryEntity c set c.visible=false ")
    Integer delete(Integer id);

    @Query("select new com.example.mapper.ArticleMapper(c.id,c.key,c.name_uz) from CatagoryEntity c where c.key=?1")
    List<ArticleMapper>getByUz(String key);

    @Query("select new com.example.mapper.ArticleMapper(c.id,c.key,c.name_ru) from CatagoryEntity c where c.key=?1")
    List<ArticleMapper> getByRU(String key);

    @Query("select new com.example.mapper.ArticleMapper(c.id,c.key,c.name_ru) from CatagoryEntity c where c.key=?1")
    List<ArticleMapper> getByEng(String key);

    @Query("select c from CatagoryEntity c where c.key=?1")
    CatagoryEntity findByKey(String key);

    Optional<CatagoryEntity> findByIdAndVisibleTrue(Integer catgoryId);



}
