package com.example.controller;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import com.example.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/init")
@RestController
public class InitController {

    @Autowired
    ProfileRepository profileRepository;

    @GetMapping("/admin")
    public String init(){
    if(profileRepository.findByPhone("902002020")==null){
        ProfileEntity profileEntity=new ProfileEntity();
        profileEntity.setName("Gulom");
        profileEntity.setSurname("Urolov");
        profileEntity.setPhone("902002020");
        profileEntity.setPassword(Md5Util.encode("2000"));
        profileEntity.setRole(ProfileRole.ROLE_ADMIN);
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileEntity.setCreatedDate(LocalDateTime.now());
        profileEntity.setVisible(Boolean.TRUE);
        profileRepository.save(profileEntity);
        return "done";

    }
    return "Already exists";
    }
}
