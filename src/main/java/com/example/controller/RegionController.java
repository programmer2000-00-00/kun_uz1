package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("sec/save")
    public ResponseEntity<?> save(@RequestBody RegionDTO regionDTO) {
        RegionDTO save = regionService.save(regionDTO);
        return ResponseEntity.ok(save);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("sec/update/{a}")
    public ResponseEntity<?> update(@PathVariable Integer a, @RequestBody RegionDTO regionDTO) {
        String update = regionService.update(a, regionDTO);
        return ResponseEntity.ok(update);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("sec/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(regionService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("sec/get")
    public ResponseEntity<?>getList(){
      return   ResponseEntity.ok(regionService.getList());
    }
    @GetMapping("/get/language")
    public ResponseEntity<?>getListlan(@RequestParam("lang") String language, @RequestParam("key") String key){

       return ResponseEntity.ok(regionService.getListlan(language,key));

    }
}
