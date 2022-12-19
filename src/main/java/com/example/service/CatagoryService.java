package com.example.service;

import com.example.dto.AttachDTO;
import com.example.dto.CatagoryDTO;
import com.example.dto.RegionDTO;
import com.example.entity.CatagoryEntity;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.ArticleMapper;
import com.example.repository.CatagoryRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatagoryService {
    @Autowired
    private CatagoryRepository catagoryRepository;
    @Autowired
    private ResourceBundleService resourceService;

    public CatagoryDTO save(CatagoryDTO catagoryDTO) {

        check(catagoryDTO);

        catagoryRepository.save(TOEntity(catagoryDTO, SpringSecurityUtil.getCurrentUserId()));
        catagoryDTO.setPId(SpringSecurityUtil.getCurrentUserId());
        return catagoryDTO;
    }

    public CatagoryEntity TOEntity(CatagoryDTO catagoryDTO, Integer pId) {
        CatagoryEntity entity = new CatagoryEntity();
        entity.setKey(catagoryDTO.getKey());
        entity.setName_uz(catagoryDTO.getName_uz());
        entity.setName_ru(catagoryDTO.getName_ru());
        entity.setName_eng(catagoryDTO.getName_eng());
        entity.setVisible(true);
        entity.setLocalDate(LocalDate.now());
        entity.setPId(pId);
        return entity;

    }

    public void check(CatagoryDTO catagoryDTO) {
        if (catagoryDTO.getKey() == null || catagoryDTO.getKey().trim().length() < 3) {
            throw new AppBadRequestException("key is wrong");
        }
        if (catagoryDTO.getName_uz() == null || catagoryDTO.getName_uz().trim().length() < 3) {
            throw new AppBadRequestException("name_uz is wrong");
        }
        if (catagoryDTO.getName_ru() == null || catagoryDTO.getName_ru().trim().length() < 3) {
            throw new AppBadRequestException("name_ru is wrong");
        }
        if (catagoryDTO.getName_eng() == null || catagoryDTO.getName_eng().trim().length() < 3) {
            throw new AppBadRequestException("name_eng is wrong");
        }

    }

    public Boolean update(Integer id, CatagoryDTO catagoryDTO) {
        Optional<CatagoryEntity> byId = catagoryRepository.findById(id);
        if (byId.isEmpty()|| !byId.get().getVisible()) {
            throw new ItemNotFoundException("this id catagory not found");
        }
        Integer integer = catagoryRepository.updateById(catagoryDTO.getKey(), catagoryDTO.getName_uz(),
                catagoryDTO.getName_ru(), catagoryDTO.getName_eng(), id);
        if (integer == 0) {
            throw new AppBadRequestException("not updated");
        }
        return true;
    }

    public String delete(Integer id) {
        Optional<CatagoryEntity> byId = catagoryRepository.findById(id);
        if (byId.isEmpty()||!byId.get().getVisible()) {
            throw new ItemNotFoundException("this id not found");
        }
        Integer delete = catagoryRepository.delete(id);
        if (delete == 0) {
            throw new AppBadRequestException("no deleted");
        }
        return "successefully deleted";

    }

    public List<CatagoryDTO> getList() {
        Iterable<CatagoryEntity> all = catagoryRepository.findAll();

        return ToDTO(all);

    }
    public List<CatagoryDTO>ToDTO(Iterable<CatagoryEntity> list){
        List<CatagoryDTO>dtoList=new ArrayList<>();
        for (CatagoryEntity entity : list) {
            CatagoryDTO dto=new CatagoryDTO();
            dto.setId(entity.getId());
            dto.setKey(entity.getKey());
            dto.setName_ru(entity.getName_uz());
            dto.setName_uz(entity.getName_uz());
            dto.setName_uz(entity.getName_eng());
            dto.setVisible(entity.getVisible());
            dto.setLocalDate(entity.getLocalDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<ArticleMapper> getListLanguage(String language, String key) {

        CatagoryEntity byKey = catagoryRepository.findByKey(key);
        if(byKey==null||!byKey.getVisible()){ throw new ItemNotFoundException("this url not found");}
        switch (language.trim().toUpperCase()) {
            case "UZ" -> {
                return catagoryRepository.getByUz(key);
            }
            case "RU" -> {
                return catagoryRepository.getByRU(key);
            }
            case "ENG" -> {
                return catagoryRepository.getByEng(key);
            }
            default -> {
                throw new AppBadRequestException("somthing is wrong");
            }
        }

    }

    public CatagoryDTO getById(Integer catgoryId, Language language) {
        Optional<CatagoryEntity> byId = catagoryRepository.findByIdAndVisibleTrue(catgoryId);
        if(byId.isEmpty()){
            throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        CatagoryEntity entity= byId.get();
        CatagoryDTO dto=new CatagoryDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (language){
            case UZ-> dto.setLanguage(entity.getName_uz());
            case RU->dto.setLanguage(entity.getName_ru());
            case ENG -> dto.setName_eng(entity.getName_eng());
        }
        return dto;
    }
}
