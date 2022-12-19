package com.example.controller;

import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload1")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        String fileName = attachService.saveToSystemOld(file);
        return ResponseEntity.ok().body(fileName);
    }

    @GetMapping(value = "/open1/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }
    @GetMapping(value = "/open_general1/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general1(@PathVariable("fileName") String fileName) {
        return attachService.open_general(fileName);
    }
    @GetMapping("/download1/{fineName}")
    public ResponseEntity<Resource> download1(@PathVariable("fineName") String fileName) {
        Resource file = attachService.download(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload1(@RequestParam("file") MultipartFile file) {
        AttachDTO dto = attachService.saveToSystemNew(file);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping(value = "/open/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] openById(@PathVariable("id") String id) {
       return attachService.openById(id);
    }
    @GetMapping(value = "/open_general/{id}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("id") String id) {
        return attachService.open_general1(id);
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id) {
        Resource file = attachService.download(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/upload/get")
    public ResponseEntity<?> getByAdmin(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<AttachDTO> byAdmin = attachService.getByAdmin(page, size);
        return ResponseEntity.ok().body(byAdmin);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        String file = attachService.delete(id);
        return ResponseEntity.ok(file);
    }




}
