package com.example.controller;

import com.example.dto.CatagoryDTO;
import com.example.enums.ProfileRole;
import com.example.mapper.ArticleMapper;
import com.example.service.CatagoryService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catagory")
public class CatagoryController {
    @Autowired
    private CatagoryService catagoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("sec/save")
    public ResponseEntity<?>save(@RequestBody CatagoryDTO catagoryDTO){
        CatagoryDTO save = catagoryService.save(catagoryDTO);
        return ResponseEntity.ok(save);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("sec/update/{id}")
    public ResponseEntity<?>update(@PathVariable Integer id,@RequestBody CatagoryDTO catagoryDTO)
    {
        Boolean update = catagoryService.update(id, catagoryDTO);
        return ResponseEntity.ok(update);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("sec/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
       return  ResponseEntity.ok(catagoryService.delete(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("sec/get")
    public ResponseEntity<?>get(){
        return ResponseEntity.ok(catagoryService.getList());
    }
    @GetMapping("get/language")
    public ResponseEntity<?>getList(@RequestParam("language") String language,@RequestParam("key") String key ){

       return ResponseEntity.ok(catagoryService.getListLanguage(language,key));
    }
}
