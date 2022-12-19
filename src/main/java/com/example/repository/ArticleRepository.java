package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<ArticleEntity,String>, PagingAndSortingRepository<ArticleEntity,String> {

    @Transactional
    @Modifying
    @Query("update ArticleEntity a set a.title=?1,a.description=?2,a.content=?3,a.sharedCount=?4,a.imageId=?5,a.regionId=?6,a.catagoryId=?7 where a.id=?8 ")
    Integer update(String title, String description, String content, Integer sharedCount, String imageId, Integer regionId, Integer catagoryId,String id);
    @Transactional
    @Modifying
    @Query("update ArticleEntity a set a.visible= false where a.id=?1 ")
    Integer update1(String id);

    @Transactional
    @Modifying
    @Query("update ArticleEntity a set a.status=?1 where a.id=?2")
     Integer updateByStatus (ArticleStatus published,String id);
    @Query(value = "select a.id as id,a.title as title ,a.description as description ,a.image_id as imageId,a.published_date as publishedDate from " +
            "article as a inner join article_type as  at on a.article_type_id=at.id where at.id=?1 and a.status='PUBLISHED' order by a.created_date desc limit 5",nativeQuery = true)
    List<ArticleShortInfoMapper> findByStatus(Integer id);

    @Query(value = "select a.id as id,a.title as title ,a.description as description ,a.image_id as imageId,a.published_date as publishedDate from " +
            "article as a inner join article_type as  at on a.article_type_id=at.id where at.id=?1 and a.id not in(?2) and a.status='PUBLISHED' order by a.created_date desc limit 8",nativeQuery = true)
    List<ArticleShortInfoMapper> findBy8id(Integer id,List<String> idlist);

    Optional<ArticleEntity> findByIdAndStatusAndVisibleTrue(String id, ArticleStatus published);

    List<ArticleEntity> findByArticleTypeIdAndStatusAndVisibleTrue(Integer id, ArticleStatus published);

    @Query("select  new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from " +
            "ArticleEntity  a inner join a.articleType at where a.articleTypeId=at.id and  at.id=?1 and a.id<> ?2 and a.status='PUBLISHED'")
    List<ArticleShortInfoMapper> getByExceptId(Integer typeId, String id, PageRequest pr);


    @Query("select new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from ArticleEntity a where a.status=:published and a.visible=true" )
    List<ArticleShortInfoMapper> findByViewCount(PageRequest pr, ArticleStatus published);
    @Query("select new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from ArticleEntity a where a.articleTypeId=?1 and a.regionId=?2 and a.status='PUBLISHED' and a.visible=true")
    List<ArticleShortInfoMapper>findByTypIdAndKey(Integer typeId, Integer id, PageRequest pr);
    @Query("select new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from ArticleEntity a where a.regionId=?1 and a.status='PUBLISHED' and a.visible=true")
    Page<ArticleShortInfoMapper> getByRegionId(Integer id, Pageable pageable);
    @Query("select new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from ArticleEntity a where a.catagoryId=?1 and a.status='PUBLISHED' and a.visible=true")
     List<ArticleShortInfoMapper>getByCategoryKey(Integer id, PageRequest pr);

    @Query("select new com.example.mapper.ArticleShortInfoMapper(a.id,a.title,a.description,a.imageId,a.publishedDate) from ArticleEntity a where a.catagoryId=?1 and a.status='PUBLISHED' and a.visible=true")
    Page<ArticleShortInfoMapper> getByCategoryId(Integer id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ArticleEntity a set a.viewCount=a.viewCount+1 where a.id=:articleId and a.status='PUBLISHED' and a.visible=true")
    Integer updateByArticleId(@Param("articleId") String articleId);
    @Transactional
    @Modifying
    @Query("update ArticleEntity a set a.sharedCount=a.sharedCount+1 where a.id=:articleId and a.status='PUBLISHED' and a.visible=true")
    Integer updateByArticleId1(String articleId);
}
