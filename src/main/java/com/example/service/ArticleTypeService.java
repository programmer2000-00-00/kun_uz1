package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.exp.AppBadRequestException;
import com.example.exp.AppForbiddenException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.ArticleMapper;
import com.example.repository.ArticleTypeRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository repository;

    public ArticleTypeDTO save(ArticleTypeDTO articleTypeDTO) {
        checkParameters(articleTypeDTO);
        ArticleTypeEntity entity = toEntity(articleTypeDTO);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        repository.save(entity);
        articleTypeDTO.setId(entity.getPrtId());
        articleTypeDTO.setPrtId(entity.getPrtId());
        return articleTypeDTO;

    }

    private static ArticleTypeEntity toEntity(ArticleTypeDTO articleTypeDTO) {

        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setKey(articleTypeDTO.getKey());
        entity.setName_uz(articleTypeDTO.getName_uz());
        entity.setName_en(articleTypeDTO.getName_en());
        entity.setName_ru(articleTypeDTO.getName_ru());
        entity.setCreatedDate(LocalDate.now());
        return entity;
    }

    private void checkParameters(ArticleTypeDTO dto) {
        if (dto.getKey() == null || dto.getKey().trim().isEmpty() || dto.getKey().trim().length() < 3) {
            throw new AppBadRequestException("Key Not Valid");
        }
        if (dto.getName_en() == null || dto.getName_en().trim().isEmpty() || dto.getName_en().trim().length() < 3) {
            throw new AppBadRequestException("name en Not Valid");
        }

        if (dto.getName_uz() == null || dto.getName_uz().trim().isEmpty() || dto.getName_uz().trim().length() < 3) {
            throw new AppBadRequestException("name uz Not Valid");
        }
        if (dto.getName_ru() == null || dto.getName_ru().trim().isEmpty() || dto.getName_ru().trim().length() < 3) {
            throw new AppBadRequestException("name ru Not Valid");
        }
    }


    public ArticleTypeDTO updateById(Integer id, ArticleTypeDTO articleTypeDTO) {

        if (repository.findById(id).isEmpty()||!articleTypeDTO.getVisible()) {
            throw new ItemNotFoundException("this id articleType not found");
        }
        int i = repository.updateById1(articleTypeDTO.getKey(), articleTypeDTO.getName_uz(), articleTypeDTO.getName_ru(), articleTypeDTO.getName_en(), id);
        if (i == 0) {
            throw new AppBadRequestException("something is wrong");
        }
        articleTypeDTO.setId(id);
        return articleTypeDTO;
    }

    public Boolean deleteById(Integer id) {

        Optional<ArticleTypeEntity> byId = repository.findById(id);
        if (byId.isEmpty() || !byId.get().getVisible()) {
            throw new ItemNotFoundException("this id article type not found");
        }
        Integer integer = repository.deleteByIdd(id);
        if(integer!=0) {
            return true;
        }
        return false;

    }

    public Page<ArticleTypeDTO> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleTypeEntity> entities = repository.findAll(pageable);
        List<ArticleTypeEntity> content = entities.getContent();
        Long totalPage = entities.getTotalElements();

        return new PageImpl<>(toDTO(content), pageable, totalPage);
    }

    private List<ArticleTypeDTO> toDTO(List<ArticleTypeEntity> content) {

        List<ArticleTypeDTO> dtoList = new ArrayList<>();
        for (ArticleTypeEntity entity : content) {
            ArticleTypeDTO dto = new ArticleTypeDTO();
            dto.setKey(entity.getKey());
            dto.setName_en(entity.getName_en());
            dto.setName_ru(entity.getName_ru());
            dto.setName_uz(entity.getName_uz());
            dto.setCreatedDate(entity.getCreatedDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ArticleMapper> getListBylan(String language, String key) {
        ArticleTypeEntity byKey = repository.findByKey(key);
        if (byKey == null || !byKey.getVisible()) {
            throw new ItemNotFoundException("this ur not found");
        }
        switch (language.trim().toUpperCase()) {
            case "UZ" -> {
                return repository.getByListLangUz(key);
            }
            case "ENG" -> {
                return repository.getByListLangEng(key);
            }
            case "RU" -> {
                return repository.getByListLangRu(key);
            }
            default -> {
                throw new AppForbiddenException("Language is invalid!!");
            }

        }
    }
}
