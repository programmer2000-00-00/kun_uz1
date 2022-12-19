package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.enums.ProfileRole;
import com.example.mapper.ArticleMapper;
import com.example.service.ArticleTypeService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sec/save")
    public ResponseEntity<?> save( @RequestBody ArticleTypeDTO articleTypeDTO){
        ArticleTypeDTO save = articleTypeService.save(articleTypeDTO);
        return ResponseEntity.ok(save);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("sec/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody ArticleTypeDTO articleTypeDTO){
        ArticleTypeDTO result = articleTypeService.updateById(id, articleTypeDTO);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("sec/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Boolean b=articleTypeService.deleteById(id);
        return ResponseEntity.ok(b);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("sec/get")
    public ResponseEntity<?>getList(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
        Page<ArticleTypeDTO> list = articleTypeService.getList(page, size);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/get")
    public ResponseEntity<?>getListByLan(@RequestParam("language")String language,@RequestParam String key){
        List<ArticleMapper> listBylan = articleTypeService.getListBylan(language,key);
        return ResponseEntity.ok(listBylan);
    }

}
