package com.example.service;

import com.example.dto.RegionDTO;
import com.example.dto.RegistrDTO;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.AppForbiddenException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.RegionMapper;
import com.example.repository.RegionRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private ResourceBundleService resourceService;
    @Autowired
    private RegionRepository regionRepository;


    public RegionDTO save(RegionDTO regionDTO) {
        check(regionDTO);
        regionRepository.save(ToEntity(regionDTO, SpringSecurityUtil.getCurrentUserId()));
        regionDTO.setId(ToEntity(regionDTO,SpringSecurityUtil.getCurrentUserId()).getId());
        regionDTO.setPId(SpringSecurityUtil.getCurrentUserId());
        return regionDTO;
    }
    public RegionEntity ToEntity(RegionDTO regionDTO,Integer pId){
        RegionEntity regionEntity=new RegionEntity();
        regionEntity.setKey(regionDTO.getKey());
        regionEntity.setName_uz(regionDTO.getName_uz());
        regionEntity.setName_ru(regionDTO.getName_ru());
        regionEntity.setName_eng(regionDTO.getName_eng());
        regionEntity.setVisible(true);
        regionEntity.setPId(pId);
        regionEntity.setLocalDate(LocalDate.now());

        return regionEntity;
    }
    public void check(RegionDTO regionDTO){
        if(regionDTO.getKey()==null||regionDTO.getKey().trim().length()<3){
            throw new AppBadRequestException("key is wrong");
        }
        if(regionDTO.getName_uz()==null||regionDTO.getName_uz().trim().length()<3){
            throw new AppBadRequestException("name_uz is wrong");
        }
        if(regionDTO.getName_ru()==null||regionDTO.getName_ru().trim().length()<3){
            throw new AppBadRequestException("name_ru is wrong");
        }
        if(regionDTO.getName_eng()==null||regionDTO.getName_eng().trim().length()<3){
            throw new AppBadRequestException("name_eng is wrong");
        }

    }

    public String update(Integer id, RegionDTO regionDTO) {
        Optional<RegionEntity> byId = regionRepository.findById(id);
        if(byId.isEmpty()||!byId.get().getVisible()){
            throw new ItemNotFoundException("this id region not found");
        }
        Integer integer = regionRepository.updateByID(regionDTO.getKey(), regionDTO.getName_uz(),
                regionDTO.getName_ru(), regionDTO.getName_eng(), id);
        if(integer==0){
            throw new AppBadRequestException("update is not");
        }
        return "successefully updated";
    }

    public String delete(Integer id) {
        Optional<RegionEntity> byId = regionRepository.findById(id);
        if(byId.isEmpty()||!byId.get().getVisible()){
            throw new ItemNotFoundException("this id region not found");
        }
        regionRepository.deleteById(id);
        return "successefully deleted";
    }


    public List<RegionDTO> getList() {

        Iterable<RegionEntity> all = regionRepository.findAll();
        return toDTO(all);

    }
    public List<RegionDTO> toDTO(Iterable<RegionEntity>list){
          List<RegionDTO>dtoList=new ArrayList<>();
        for (RegionEntity regionEntity : list) {
            RegionDTO dto=new RegionDTO();
            dto.setKey(regionEntity.getKey());
            dto.setName_uz(regionEntity.getName_uz());
            dto.setName_ru(regionEntity.getName_ru());
            dto.setName_eng(regionEntity.getName_eng());
            dto.setVisible(regionEntity.getVisible());
            dto.setLocalDate(regionEntity.getLocalDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<RegionMapper> getListlan(String language,String key) {

        RegionEntity byKey = regionRepository.findByKey(key);
        if(byKey==null||!byKey.getVisible()){
            throw new ItemNotFoundException("this url not found");
        }

        switch (language.trim().toUpperCase()) {
            case "UZ" -> {
                return regionRepository.getByUz(key);
            }
            case "RU" -> {
                return regionRepository.getByRu(key);
            }
            case "ENG" -> {
                return regionRepository.getByEng(key);
            }
            default -> {
                throw new AppForbiddenException("something is wrong");
            }
        }
    }

    public RegionDTO getById(Integer regionId, Language language) {
        Optional<RegionEntity> byId = regionRepository.findByIdAndVisibleTrue(regionId);
        if(byId.isEmpty()){
        throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        RegionEntity entity= byId.get();
        RegionDTO dto=new RegionDTO();
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
