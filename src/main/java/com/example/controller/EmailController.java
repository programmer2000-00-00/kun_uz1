package com.example.controller;

import com.example.dto.EmailDTO;
import com.example.entity.EmailEntity;
import com.example.service.EmailService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @GetMapping("/get")
    public ResponseEntity<?>getByEmail(@RequestParam("email") String email){
        List<EmailDTO> byEmail = emailService.getByEmail(email);
        return ResponseEntity.ok(byEmail);
    }
    @GetMapping("/get/data")
    public ResponseEntity<?>getByData(@RequestParam("data") String data, HttpServletRequest request){

        JwtUtil.getIdFromHeader(request);
        List<EmailDTO> byData = emailService.getByData(data);

        return ResponseEntity.ok(byData);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/page")
    public ResponseEntity<?>getByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size){


       return ResponseEntity.ok(emailService.getByPage(page,size));
    }

}
