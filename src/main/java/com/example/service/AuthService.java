package com.example.service;

import com.example.dto.AuthorazitionDTO;
import com.example.dto.AuthorazitionResponseDTO;
import com.example.dto.RegistrDTO;
import com.example.entity.EmailEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.exp.PhoneAlreadyExistsException;
import com.example.repository.EmailRepositiry;
import com.example.repository.ProfileRepository;
import com.example.util.JwtTokenUtil;
import com.example.util.JwtUtil;
import com.example.util.Md5Util;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private EmailService emailService;
    @Autowired
    EmailRepositiry emailRepositiry;

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceService;
    public String   registration(RegistrDTO registrDTO) {

        check(registrDTO);

        ProfileEntity byPhone = profileRepository.findByPhone(registrDTO.getPhone());
        if (byPhone != null) {
            if (byPhone.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRepository.delete(byPhone);
            } else {
                throw new PhoneAlreadyExistsException("this number exists");
            }
        }
        ProfileEntity byEmail = profileRepository.findByEmail(registrDTO.getEmail());
        if (byPhone != null) {
            if (byPhone.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRepository.delete(byEmail);
            } else {
                throw new PhoneAlreadyExistsException("this number exists");
            }
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(registrDTO.getName());
        profileEntity.setSurname(registrDTO.getSurname());
        profileEntity.setPhone(registrDTO.getPhone());
        profileEntity.setPassword(Md5Util.encode(registrDTO.getPassword()));
        profileEntity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileEntity.setRole(ProfileRole.ROLE_USER);
        profileEntity.setEmail(registrDTO.getEmail());

        profileRepository.save(profileEntity);

        StringBuilder sb = new StringBuilder();
        sb.append("<h1 style=\\\"text-align: center\\\">Salom Qalaysan</h1>");
        String link = String.format("<a href=\"http://192.168.59.235/auth/verification/email/%s\"> Click there</a>", JwtUtil.encode(profileEntity.getId()));
        sb.append(link);
        emailService.sendEmailMine(registrDTO.getEmail(), "Complite Registration", sb.toString());
        EmailEntity email=new EmailEntity();
        email.setMessage("Salom Qalaysan");
        email.setEmail(profileEntity.getEmail());
        email.setLocalDateTime(LocalDateTime.now());
        emailRepositiry.save(email);


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
        return "Emailga link ketdi aka tekwiring qani";
    }
    public void check(RegistrDTO registrDTO){
        if(registrDTO.getName()==null||registrDTO.getName().trim().length()<=3){
            throw new AppBadRequestException("Name is wrong");
        }
        if(registrDTO.getSurname()==null||registrDTO.getSurname().trim().length()<3){
            throw new AppBadRequestException("surname is wrong");
        }
        if(registrDTO.getPhone()==null||registrDTO.getPhone().trim().length()<3){
            throw new AppBadRequestException("phone is wrong");
        }
        if(registrDTO.getPassword()==null||registrDTO.getPassword().trim().length()<3){
            throw new AppBadRequestException("password is wrong");
        }

    }

    public AuthorazitionResponseDTO login(AuthorazitionDTO authorazitionDTO, Language language) {

        Optional<ProfileEntity> byPhoneAndPassword = profileRepository.findByPhoneAndPassword(authorazitionDTO.getPhone(), Md5Util.encode(authorazitionDTO.getPassword()));

        if(byPhoneAndPassword.isEmpty()){
        throw new ItemNotFoundException(resourceService.getMessage("credential.wrong", language.name()));
        }
        if(byPhoneAndPassword.get().getStatus()!=ProfileStatus.ACTIVE){
            throw new ItemNotFoundException("this user not active");
        }
        if(!byPhoneAndPassword.get().getVisible()){
            throw new ItemNotFoundException("this user not found");
        }
        AuthorazitionResponseDTO authorazitionResponseDTO=new AuthorazitionResponseDTO();
        authorazitionResponseDTO.setName(byPhoneAndPassword.get().getName());
        authorazitionResponseDTO.setSurname(byPhoneAndPassword.get().getSurname());
        authorazitionResponseDTO.setRole(byPhoneAndPassword.get().getRole());
        authorazitionResponseDTO.setToken(JwtTokenUtil.encode(byPhoneAndPassword.get().getPhone(),byPhoneAndPassword.get().getRole()));
        return authorazitionResponseDTO;
    }

    public String verification(String jwt) {
        Integer id;
        try {
            id = JwtUtil.decodeForEmailVerification(jwt);
        } catch (JwtException e) {
            return "Verification failed";
        }

        Optional<ProfileEntity> exists = profileRepository.findById(id);
        if (!exists.get().getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            return "Verification failed";
        }
        exists.get().setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(exists.get());


        return "Verification success";
    }
}
