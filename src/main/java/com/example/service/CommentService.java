package com.example.service;

import com.example.dto.CommentDTO;
import com.example.entity.CommentEntity;
import com.example.enums.Language;
import com.example.exp.ItemNotFoundException;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ResourceBundleService resourceService;
    @Autowired
     private ArticleService articleService;

    public String save(CommentDTO dto, Language language) {
        CommentEntity entity=new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId().toString());
        entity.setReplyId(dto.getReplyId());
        commentRepository.save(entity);
        return "Successefully saved";
    }

    public String update(CommentDTO dto, Integer id, Language language) {
        Optional<CommentEntity> byId = commentRepository.findById(id);
        if(byId.isEmpty()){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        articleService.findById(dto.getArticleId());
        Integer integer = commentRepository.updateByid(dto.getContent(), dto.getArticleId(),id);
        if(integer!=0){
            return "Successefully updated";
        }
        return  null;
    }
}
