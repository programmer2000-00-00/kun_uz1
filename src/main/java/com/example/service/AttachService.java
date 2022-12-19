package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exp.AttachException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttachService {
    @Value("${attach.upload.folder}")
    private String attachFolder;
    @Value("${application.url}")
    private String attachOpenUrl;
     @Value("${attach.open.url}")
    private String getAttachOpenUrl;
    @Autowired
    private AttachRepository attachRepository;
    public String saveToSystemOld(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public byte[] loadImage(String fileName) {
        byte[] imageInByte;
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
    public byte[] open_general(String fileName) {
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + fileName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource download1(String fileName) {
        try {
            Path file = Paths.get("attaches/" + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    public String saveToSystem(MultipartFile file) {
        try {
            // attaches/2022/04/23/UUID.png
            String attachPath = getYmDString(); // 2022/04/23
            String extension = getExtension(file.getOriginalFilename()); // .png....
            String fileName = UUID.randomUUID().toString() + "." + extension; // UUID.png

            File folder = new File(attachFolder + attachPath);  // attaches/2022/04/23/
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + attachPath + "/" + fileName); // attaches/2022/04/23/UUID.png
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public AttachDTO saveToSystemNew(MultipartFile file) {
        try {
            // attaches/2022/04/23/UUID.png
            String attachPath = getYmDString(); // 2022/04/23
            String extension = getExtension(file.getOriginalFilename()); // .png....
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "." + extension; // UUID.png

            File folder = new File(attachFolder + attachPath);  // attaches/2022/04/23/
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + attachPath + "/" + fileName); // attaches/2022/04/23/UUID.png
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setPath(attachPath);
            entity.setExtension(extension);
            entity.setSize(file.getSize());
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedData(LocalDateTime.now());
            entity.setId(uuid);
            attachRepository.save(entity);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(entity.getId());
            attachDTO.setOriginalName(file.getOriginalFilename());
            //...
            attachDTO.setOpenUrl(attachOpenUrl + fileName);

            return attachDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public byte[] openById(String id) {
        byte[] imageInByte;
        BufferedImage originalImage;
        try {

            Optional<AttachEntity> byId = attachRepository.findById(id);
            if(byId.isEmpty()){
                throw new AttachException("this id is not exists");
            }
            originalImage = ImageIO.read(new File(attachFolder+byId.get().getPath()+"/"+
                    byId.get().getId()+"."+byId.get().getExtension()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];

    }
    public byte[] open_general1(String id) {
        byte[] data;
        try {
            Optional<AttachEntity> byId = attachRepository.findById(id);
            if(byId.isEmpty()){
                throw new AttachException("this id is not exists");
            }
            Path file = Paths.get(attachFolder+byId.get().getPath()+"/"+
                    byId.get().getId()+"."+byId.get().getExtension());
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
    public Resource download(String id) {
        try {
            Optional<AttachEntity> byId = attachRepository.findById(id);
            if(byId.isEmpty()){
                throw new AttachException("this id is not exists");
            }
            Path file = Paths.get(attachFolder+byId.get().getPath()+"/"+
                    byId.get().getId()+"."+byId.get().getExtension());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Page<AttachDTO> getByAdmin(Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size);
        Page<AttachEntity> list = attachRepository.findAll(pageable);
        List<AttachEntity>content=list.getContent();
        Long totalElement= list.getTotalElements();
        List<AttachDTO> dtoList=new ArrayList<>();
        for (AttachEntity attach : content) {
            AttachDTO dto=new AttachDTO();
            dto.setId(attach.getId());
            dto.setOriginalName(attach.getOriginalName());
            dto.setPath(attach.getPath());
            dto.setOpenUrl(getAttachOpenUrl+attach.getId());
            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList,pageable,totalElement);
    }

    public String delete(String id) {

        Optional<AttachEntity> byId = attachRepository.findById(id);
        if(byId.isEmpty()){
            throw new AttachException("this id attach no tfound");
        }
        attachRepository.deleteById(id);
        return "success deleted";
    }
}


