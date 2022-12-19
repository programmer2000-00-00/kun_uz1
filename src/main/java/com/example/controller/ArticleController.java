package com.example.controller;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleFilterDTO;
import com.example.dto.profile.ProfileFilterDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/sec/save")
    public ResponseEntity<?> save(@RequestBody ArticleDTO articleDTO) {
        String save = articleService.save(articleDTO);
        return ResponseEntity.ok(save);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/sec/update/{id}")
    public ResponseEntity<?> update( @RequestBody ArticleDTO articleDTO, @PathVariable String id) {
        String save = articleService.update(articleDTO, id);
        return ResponseEntity.ok(save);
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/sec/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        String save = articleService.delete(id);
        return ResponseEntity.ok(save);
    }
    @PreAuthorize("hasRole('PUBLISHER')")
    @PutMapping("/sec/status/{id}")
    public ResponseEntity<?> status(@PathVariable String id) {
        return ResponseEntity.ok(articleService.change(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getByArticleType(id));
    }

    @GetMapping("get/list/{id}")
    public ResponseEntity<?> getList(@PathVariable("id") Integer id, @RequestBody List<String> idlist) {
        List<ArticleDTO> byGivenId = articleService.getByGivenId(id, idlist);
        return ResponseEntity.ok(byGivenId);
    }

    @GetMapping("get/lang")
    public ResponseEntity<?> get(@RequestParam("id") String id, @RequestParam("lang") Language language) {
        ArticleDTO getByIdannLang = articleService.getByIdAndLanguage(id, language);
        return ResponseEntity.ok(getByIdannLang);
    }

    @GetMapping("get/last4")
    public ResponseEntity<?> get(@RequestParam("typeId") Integer typeId, @RequestParam("id") String id,
                                 @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {

        List<ArticleDTO> response = articleService.getByTypeAndId(typeId, id, language);
        return ResponseEntity.ok(response);
    }

    @GetMapping("get/last4/most")
    public ResponseEntity<?> getList(@RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {
        List<ArticleShortInfoMapper> mostArticle = articleService.getMostArticle(language);
        return ResponseEntity.ok(mostArticle);
    }

    @GetMapping("get/last/type/region")
    public ResponseEntity<?> getList1(@RequestParam("id") Integer id, @RequestParam("key") String key, @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {
        List<ArticleShortInfoMapper> byTypeAndRegionKey = articleService.getByTypeAndRegionKey(id, key, language);

        return ResponseEntity.ok(byTypeAndRegionKey);
    }

    @GetMapping("get/key")
    public ResponseEntity<?> getList2(@RequestParam("key") String key, @RequestParam("page") Integer page, @RequestParam("size") Integer size,
                                      @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {
        Page<ArticleShortInfoMapper> byTypeAndRegionKey = articleService.getByRegionKey(key, page, size, language);

        return ResponseEntity.ok(byTypeAndRegionKey);
    }

    @GetMapping("get/catagory/key")
    public ResponseEntity<?> getList3(@RequestParam("key") String key, @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {
        List<ArticleShortInfoMapper> byTypeAndRegionKey = articleService.getByCatagoryKey(key, language);

        return ResponseEntity.ok(byTypeAndRegionKey);
    }

    @GetMapping("get/catagory/key/page")
    public ResponseEntity<?> getList4(@RequestParam("key") String key, @RequestParam("page") Integer page,
                                      @RequestParam("size") Integer size,
                                      @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language) {
        Page<ArticleShortInfoMapper> byCatagoryKeyAndPage = articleService.getByCatagoryKeyAndPage(key, page, size, language);
        return ResponseEntity.ok(byCatagoryKeyAndPage);
    }

    @PutMapping("update/view/count")
    public ResponseEntity<?> getList5(@RequestParam("articleId") String articleId,
                                      @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language){

        return ResponseEntity.ok(articleService.updateByArticleId(articleId,language));

    }
    @PutMapping("update/share/count")
    public ResponseEntity<?> getList6(@RequestParam("articleId") String articleId,
                                      @RequestHeader(name = "Accept-Language", defaultValue = "ENG") Language language){

        return ResponseEntity.ok(articleService.updateByArticleId1(articleId,language));
    }
    @PreAuthorize("hasRole('PUBLISHER')")
    @PostMapping("/sec/filter")
    public ResponseEntity<?> update(@RequestBody ArticleFilterDTO filterDTO, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        articleService.filter(filterDTO,page,size);
        return ResponseEntity.ok().build();
    }
}
