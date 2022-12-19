package com.example.service;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleFilterDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.ArticleRepository;
import com.example.repository.ArticleTypeRepository;
import com.example.repository.CatagoryRepository;
import com.example.repository.RegionRepository;
import com.example.repository.custom.ArticleCustomRepository;
import com.example.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ResourceBundleService resourceService;
    @Autowired
    private CatagoryRepository catagoryRepository;
    @Autowired
    private CatagoryService catagoryService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTypeRepository articleType;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;

    public String save(ArticleDTO articleDTO) {
        Optional<ArticleTypeEntity> byId = articleType.findById(articleDTO.getArticleTypeId());
        if (byId.isEmpty()) {
            throw new ItemNotFoundException("this id article type not found");
        }
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(articleDTO.getTitle());
        entity.setDescription(articleDTO.getDescription());
        entity.setContent(articleDTO.getContent());
        entity.setSharedCount(articleDTO.getSharedCount());
        entity.setImageId(articleDTO.getImageId());
        entity.setRegionId(articleDTO.getRegionId());
        entity.setCatagoryId(articleDTO.getCatagoryId());
        entity.setStatus(ArticleStatus.NOTPUBLISHED);
        entity.setModeratorId(SpringSecurityUtil.getCurrentUserId());
        entity.setArticleTypeId(articleDTO.getArticleTypeId());

        try {
            articleRepository.save(entity);
            return "Successefully saved";
        } catch (DataIntegrityViolationException e) {
            throw new ItemNotFoundException("image id or region id or category id is not present");
        }

    }

    public String update(ArticleDTO articleDTO, String id) {
        Integer update = 0;
        findById(id);
        try {
            update = articleRepository.update(articleDTO.getTitle(), articleDTO.getDescription(), articleDTO.getContent(),
                    articleDTO.getSharedCount(), articleDTO.getImageId(), articleDTO.getRegionId(), articleDTO.getCatagoryId(), id);
        } catch (DataIntegrityViolationException e) {
            throw new ItemNotFoundException("image id or region id or category id is not present");
        }

        if (update < 1)
            return "Not Update";

        return "Successefully updated";

    }

    public String delete(String id) {

        findById(id);
        Integer integer = articleRepository.update1(id);
        if (integer != 0) {
            return "successefully deleted";
        }
        return "not updated";

    }

    public String change(String id) {
        findById(id);
        Integer integer = articleRepository.updateByStatus(ArticleStatus.PUBLISHED, id);
        if (integer != 0) return "successefy changed";
        return "not changed";

    }

    public void findById(String id) {
        articleRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("id is wrong");
        });

    }

    public List<ArticleShortInfoMapper> getByArticleType(Integer id) {
        Optional<ArticleTypeEntity> byId = articleType.findById(id);
        if (byId.isEmpty()) {
            throw new ItemNotFoundException("this id not found");
        }
        List<ArticleShortInfoMapper> byStatus = articleRepository.findByStatus(id);

        return byStatus;
    }

    public List<ArticleDTO> getByGivenId(Integer id, List<String> idlist) {

        List<ArticleShortInfoMapper> byid = articleRepository.findBy8id(id, idlist);
        return toArticleDTO(byid);
    }

    public List<ArticleDTO> toArticleDTO(List<ArticleShortInfoMapper> byId) {
        List<ArticleDTO> articleDTO = new ArrayList<>();
        for (ArticleShortInfoMapper articleShortInfoMapper : byId) {
            ArticleDTO dto = new ArticleDTO();

            dto.setTitle(articleShortInfoMapper.getTitle());
            dto.setDescription(articleShortInfoMapper.getDescription());
            dto.setImageId(articleShortInfoMapper.getImageId());
            dto.setPublishedDate(articleShortInfoMapper.getPublishedDate().atStartOfDay());
            articleDTO.add(dto);
        }
        return articleDTO;
    }

    public ArticleDTO getByIdAndLanguage(String id, Language language) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndStatusAndVisibleTrue(id, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }

        ArticleEntity entity = optional.get();
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setSharedCount(entity.getSharedCount());
        dto.setRegion(regionService.getById(entity.getRegionId(), language));
        dto.setCatagory(catagoryService.getById(entity.getCatagoryId(), language));
        return dto;

    }

    public List<ArticleDTO> getByTypeAndId(Integer typeId, String id, Language language) {

        ArticleTypeEntity entity = articleType.findByIdAndVisibleTrue(typeId);
        if (typeId == null) {
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        PageRequest pr = PageRequest.of(0, 4, sort);

//        List<ArticleEntity>entityList=articleRepository.findByArticleTypeIdAndStatusAndVisibleTrue(entity.getId(),ArticleStatus.PUBLISHED);

        List<ArticleShortInfoMapper> byExceptId = articleRepository.getByExceptId(typeId, id, pr);
        return toArticleDTO(byExceptId);
    }

    public List<ArticleShortInfoMapper> getMostArticle(Language language) {

        Sort sort = Sort.by(Sort.Direction.DESC,"viewCount");
        PageRequest pr=PageRequest.of(0,5,sort);

        List<ArticleShortInfoMapper>byViewCount=articleRepository.findByViewCount(pr,ArticleStatus.PUBLISHED);
        if(byViewCount.isEmpty()){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        return byViewCount;

    }

    public List<ArticleShortInfoMapper> getByTypeAndRegionKey(Integer typeId, String key, Language language) {
        Sort sort=Sort.by(Sort.Direction.DESC,"createdDate");
        PageRequest pr=PageRequest.of(0,6,sort);
        RegionEntity byKey = regionRepository.findByKey(key);

        List<ArticleShortInfoMapper> byTypIdAndKey = articleRepository.findByTypIdAndKey(typeId, byKey.getId(), pr);
        if(byTypIdAndKey.isEmpty()){
         throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }


        return byTypIdAndKey;
    }

    public Page<ArticleShortInfoMapper> getByRegionKey(String key, Integer page, Integer size, Language language) {
        Pageable pageable=PageRequest.of(page,size);
        RegionEntity byKey = regionRepository.findByKey(key);
        Page<ArticleShortInfoMapper> list=articleRepository.getByRegionId(byKey.getId(),pageable);
        List<ArticleShortInfoMapper>content=list.getContent();
        Long totalElemnt=list.getTotalElements();

        PageImpl<ArticleShortInfoMapper> articleShortInfoMappers = new PageImpl<>(content, pageable, totalElemnt);
        return articleShortInfoMappers;
    }

    public List<ArticleShortInfoMapper> getByCatagoryKey(String key, Language language) {
        CatagoryEntity byCatagoryKey = catagoryRepository.findByKey(key);
        log.warn("Category not found.key{}"+key);
        if(byCatagoryKey==null){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
                 }
        Sort sort = Sort.by(Sort.Direction.DESC,"createdDate");
        PageRequest pr=PageRequest.of(0,5,sort);

        List<ArticleShortInfoMapper> byCategoryKey1 = articleRepository.getByCategoryKey(byCatagoryKey.getId(), pr);
        if(byCategoryKey1.isEmpty()){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
         return byCategoryKey1;
    }

    public Page<ArticleShortInfoMapper> getByCatagoryKeyAndPage(String key, Integer page, Integer size, Language language) {
        CatagoryEntity byCatagoryKey = catagoryRepository.findByKey(key);
        if(byCatagoryKey==null){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        Pageable pageable=PageRequest.of(page,size);
        Page<ArticleShortInfoMapper> list=articleRepository.getByCategoryId(byCatagoryKey.getId(),pageable);
        List<ArticleShortInfoMapper>content=list.getContent();
        Long totalElemnt=list.getTotalElements();

        PageImpl<ArticleShortInfoMapper> articleShortInfoMappers = new PageImpl<>(content, pageable, totalElemnt);
        return articleShortInfoMappers;
    }

    public Integer updateByArticleId(String articleId, Language language) {
        Integer update = articleRepository.updateByArticleId(articleId);

        return update;
    }

    public Integer updateByArticleId1(String articleId, Language language) {
        Integer update = articleRepository.updateByArticleId1(articleId);
        return update;
    }
    public void filter(ArticleFilterDTO articleFilterDTO,Integer page,Integer size){

        Page<ArticleEntity> filter = articleCustomRepository.filter(articleFilterDTO, page, size);
         filter.forEach(entity -> {
            System.out.println(entity.getId()+" ");
            System.out.println(entity.getTitle()+" ");
            System.out.println(entity.getDescription()+" ");
            System.out.println(entity.getImageId()+" ");
            System.out.println(entity.getPublishedDate()+" ");
        });
    }

    }

