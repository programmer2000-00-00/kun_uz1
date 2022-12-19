package com.example.repository;

import com.example.entity.ArticleTypeEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleMapper;
import com.example.mapper.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity,Integer>, PagingAndSortingRepository<ArticleTypeEntity,Integer> {

    @Transactional
    @Modifying
    @Query("update ArticleTypeEntity a set a.key=?1,a.name_uz=?2,a.name_ru=?3,a.name_en=?4 where a.id=?5")
    int updateById1(String key, String name_uz, String name_ru, String name_en, Integer id);

    @Query("select new com.example.mapper.ArticleMapper(a.id,a.key,a.name_uz)  from ArticleTypeEntity a where a.key=?1")
    List<ArticleMapper> getByListLangUz(String key);
    @Query("select new com.example.mapper.ArticleMapper(a.id,a.key,a.name_ru)  from ArticleTypeEntity a where a.key=?1 ")
    List<ArticleMapper> getByListLangRu(String key);
    @Query("select new com.example.mapper.ArticleMapper(a.id,a.key,a.name_en)  from ArticleTypeEntity a where a.key=?1 ")
    List<ArticleMapper> getByListLangEng(String key);

    @Query("select a from ArticleTypeEntity a where a.key=?1")
     ArticleTypeEntity findByKey(String key);
    @Transactional
    @Modifying
    @Query("update  ArticleTypeEntity a set a.visible=false where a.id=?1")
    Integer deleteByIdd(Integer id);


    ArticleTypeEntity findByIdAndVisibleTrue(Integer typeId);
}
