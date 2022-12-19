package com.example.repository.custom;

import com.example.dto.article.ArticleFilterDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.ProfileEntity;
import com.example.mapper.ArticleShortInfoMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleCustomRepository {
    @Autowired
    private EntityManager entityManager;
   public Page<ArticleEntity> filter(ArticleFilterDTO filter, Integer page, Integer size){
       StringBuilder builder = new StringBuilder("SELECT a FROM ArticleEntity a ");
       StringBuilder countBuilder = new StringBuilder("select count(a) from ArticleEntity a ");

       Map<String,Object> params=new HashMap<>();
       if (filter.getVisible() != null) {
           builder.append(" where a.visible = ").append(filter.getVisible());
           countBuilder.append(" where a.visible = ").append(filter.getVisible());

       } else {
           builder.append(" where a.visible = true ");
           countBuilder.append(" where a.visible = true ");

       }

       if (filter.getId() != null) {
           builder.append(" And a.id =:id");
           countBuilder.append(" And a.id =:id");
           params.put("id", filter.getId());
       }
       if (filter.getTitle() != null) {
           builder.append(" And a.title =:title");
           countBuilder.append(" And a.title =:title");
           params.put("title", filter.getTitle());
       }

       if (filter.getModeratorId() != null) {
           builder.append(" And a.moderatorId =:moderatorId");
           countBuilder.append(" And a.moderatorId=:moderatorId");
           params.put("", filter.getModeratorId());
       }

       if (filter.getPublisherId() != null) {
           builder.append(" And a.publisherId =:publisherId");
           countBuilder.append(" And a.publisherId =:publisherId");
           params.put("publisherId", filter.getPublisherId());
       }

       if (filter.getStatus()!= null) {
           builder.append(" And a.status =:status");
           countBuilder.append(" And a.status =:status");
           params.put("status", filter.getStatus());
       }

       if (filter.getFromDate()!= null && filter.getToDate()!= null) {
           builder.append(" And cast(a.createdDate as date) between :fromDate  and :toDate ");
           params.put("fromDate", filter.getFromDate());
           params.put("toDate", filter.getToDate());
       } else if (filter.getFromDate() != null) { // from
           builder.append(" And a.createdDate > :fromDate ");
           params.put("fromDate",filter.getFromDate().atStartOfDay());
       } else if (filter.getToDate() != null) {
           builder.append(" And a.createdDate < :toDate ");
           params.put("toDate", filter.getToDate().atTime(LocalTime.MAX)); // 2022-12-09 23:59:59.999999999
       }
       Query query = entityManager.createQuery(builder.toString());
       query.setFirstResult((page) * size);
       query.setMaxResults(size);
       for(Map.Entry<String,Object> entry: params.entrySet()){
           query.setParameter(entry.getKey(), entry.getValue());
       }
       List<ArticleEntity> articleEntityList=query.getResultList();
       Query  countQuery = entityManager.createQuery(countBuilder.toString());
       for (Map.Entry<String, Object> entry : params.entrySet()) {
           countQuery.setParameter(entry.getKey(), entry.getValue());
       }
       Long totalElements = (Long) countQuery.getSingleResult();

       return new PageImpl<>(articleEntityList, PageRequest.of(page, size), totalElements);
   }

}
