package com.example.repository;

import com.example.entity.RegionEntity;
import com.example.mapper.RegionMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends CrudRepository<RegionEntity,Integer>, PagingAndSortingRepository<RegionEntity,Integer> {
    @Transactional
    @Modifying
    @Query("update RegionEntity a set a.key=?1,a.name_uz=?2,a.name_ru=?3,a.name_eng=?4 where a.id=?5")
    Integer updateByID(String key, String name_uz, String name_ru, String name_eng, Integer a);

    @Query("select new com.example.mapper.RegionMapper(r.id,r.key,r.name_uz) from RegionEntity r where r.key=?1")
    List<RegionMapper> getByUz(String key);
    @Query("select new com.example.mapper.RegionMapper(r.id,r.key,r.name_ru) from RegionEntity r where r.key=?1 ")
    List<RegionMapper> getByRu(String key);

    @Query("select new com.example.mapper.RegionMapper(r.id,r.key,r.name_eng) from RegionEntity r where r.key=?1")
    List<RegionMapper> getByEng(String key);

    @Query("select r from RegionEntity r where r.key=?1")
    RegionEntity findByKey(String key);

    Optional<RegionEntity> findByIdAndVisibleTrue(Integer regionId);
}
