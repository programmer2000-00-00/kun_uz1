package com.example.controller;

import com.example.dto.CommentDTO;
import com.example.enums.Language;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<?>save(@RequestBody CommentDTO dto, @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language){
      return ResponseEntity.ok(commentService.save(dto,language));
    }
    @PutMapping("/update")
    public ResponseEntity<?>update(@RequestBody CommentDTO dto,@RequestParam("id") Integer id, @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language){
        return ResponseEntity.ok(commentService.update(dto,id,language));
    }
}
