package com.example.repository;

import com.example.entity.CommentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepository extends CrudRepository<CommentEntity,Integer>, PagingAndSortingRepository<CommentEntity,Integer> {

    @Transactional
    @Modifying
    @Query("update CommentEntity c set c.content=?1,c.articleId=?2 where c.id=?3")
    Integer updateByid(String content, String articleId, Integer id);
}
