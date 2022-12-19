package com.example.config;

import com.example.entity.ProfileEntity;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        ProfileEntity byPhone = profileRepository.findByPhone(phone);
        if(byPhone==null) {
            throw new UsernameNotFoundException("Bad Creatensioanal");
        }
        return new CustomUserDetails(byPhone);
    }

}
