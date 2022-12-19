package com.example.service;

import com.example.config.CustomUserDetails;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.exp.PhoneAlreadyExistsException;
import com.example.repository.ProfileRepository;
import com.example.repository.custom.ProfileCustomRepository;
import com.example.util.Md5Util;
import com.example.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileCustomRepository profileCustomRepository;

    @Autowired
    private ProfileRepository profileRepository;


    public ProfileDTO save(ProfileDTO profileDTO) {
        checkParameters(profileDTO);
        ProfileEntity byPhone = profileRepository.findByPhone(profileDTO.getPhone());
        if (byPhone != null) {
            throw new PhoneAlreadyExistsException("this pnone already exists");
        }
        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        profileRepository.save(profileEntity);
        return profileDTO;

    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(profileDTO.getName());
        entity.setSurname(profileDTO.getSurname());
        entity.setPhone(profileDTO.getPhone());
        entity.setPassword(Md5Util.encode(profileDTO.getPassword()));
        entity.setRole(profileDTO.getRole());

        entity.setVisible(true);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());

        return entity;
    }

    private void checkParameters(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getName().trim().length() < 3) {
            throw new AppBadRequestException("Name Not Valid");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().isEmpty() || dto.getSurname().trim().length() < 3) {
            throw new AppBadRequestException("Surname Not Valid");
        }
        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty() || dto.getPhone().trim().length() < 3) {
            throw new AppBadRequestException("Email Not Valid");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty() || dto.getPassword().trim().length() < 4) {
            throw new AppBadRequestException("Password Not Valid");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("Role Not Valid");
        }
    }

    public ProfileDTO updateAdminByID(Integer id, ProfileDTO profileDTO) {
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        if(byId.isEmpty()||!byId.get().getVisible()){
            throw new ItemNotFoundException("this id profile not found");
        }
        int b = profileRepository.updateById(profileDTO.getName(), profileDTO.getSurname(), profileDTO.getStatus(),
                profileDTO.getRole(), profileDTO.getVisible(), id);
        if (b == 0) {
            throw new AppBadRequestException("something is wromg");
        }
        profileDTO.setId(id);

        return profileDTO;
    }

    public int updateAny(Integer pId, ProfileDTO profileDTO) {
        Optional<ProfileEntity> byId = profileRepository.findById(pId);
        if(byId.isEmpty()||!byId.get().getVisible()){
            throw new ItemNotFoundException("this id profile not found");
        }
        int b = profileRepository.update(profileDTO.getName(), profileDTO.getSurname(),
                Md5Util.encode(profileDTO.getPassword()), pId);

        return b;
    }

    public Page<ProfileDTO> getList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> entities = profileRepository.findAll(pageable);

        List<ProfileEntity> content = entities.getContent();
        List<ProfileDTO> dto = new LinkedList<>();
        for (ProfileEntity profileEntity : content) {
            ProfileDTO profileDTO = toDTO(profileEntity);
            dto.add(profileDTO);
        }
        return new PageImpl<>(dto, pageable, entities.getTotalElements());
    }

    private ProfileDTO toDTO(ProfileEntity profileEntity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(profileEntity.getName());
        dto.setSurname(profileEntity.getSurname());
        dto.setRole(profileEntity.getRole());
        dto.setVisible(profileEntity.getVisible());

        return dto;
    }

    public Boolean deleteById(Integer id) {
        Optional<ProfileEntity> byId = profileRepository.findById(id);
        if (byId.isEmpty() || !byId.get().getVisible()) {
            throw new ItemNotFoundException("this id not found");
        }
        Integer integer = profileRepository.deleteByIdd(id);
        if(integer==1){
            return true;
        }
        return false;
    }
    public void filter(ProfileFilterDTO filterDTO,Integer page,Integer size) {
                Page<ProfileEntity> profileEntityList = profileCustomRepository.filter(filterDTO,page,size);
                profileEntityList.forEach(entity -> {
                System.out.println(entity.getId()+" ");
                System.out.println(entity.getName()+" ");
                System.out.println(entity.getPhone()+" ");
                System.out.println(entity.getCreatedDate()+" ");
            });
        }
    }

