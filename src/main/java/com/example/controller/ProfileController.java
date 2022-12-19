package com.example.controller;

import com.example.config.CustomUserDetails;
import com.example.dto.profile.ProfileDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.JwtUtil;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/profile")
@Tag(name = "ProfileController API", description = "Api list for ProfileEntity")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/sec/save")
    public ResponseEntity<ProfileDTO> save(@RequestBody ProfileDTO profileDTO) {
        log.info("Create new Profile{}"+profileDTO);
        ProfileDTO save = profileService.save(profileDTO);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/sec/update/admin/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ProfileDTO profileDTO) {
        ProfileDTO save = profileService.updateAdminByID(id, profileDTO);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/update/any")
    public ResponseEntity<?> update( @RequestBody ProfileDTO profileDTO) {
        Integer id = SpringSecurityUtil.getCurrentUserId();
        Integer b = profileService.updateAny(id, profileDTO);
        if (b == 1) {
            return ResponseEntity.ok(b);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/sec/list")
    public ResponseEntity<Page<ProfileDTO>> getList(@RequestParam Integer page,
                                     @RequestParam Integer size) {
        Page<ProfileDTO> response = profileService.getList(page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sec/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Boolean b = profileService.deleteById(id);
        return ResponseEntity.ok(b);
    }
    @PostMapping("/sec/filter")
    public ResponseEntity<?> update(@RequestBody ProfileFilterDTO filterDTO,@RequestParam("page") Integer page,@RequestParam("size") Integer size) {
        profileService.filter(filterDTO,page,size);
        return ResponseEntity.ok().build();
    }
//    @PostMapping("/filter")
//    public ResponseEntity<?> update(@RequestBody ProfileFilterDTO filterDTO) {
//        profileService.filter(filterDTO,page,);
//        return ResponseEntity.ok().build();
//    }

}
