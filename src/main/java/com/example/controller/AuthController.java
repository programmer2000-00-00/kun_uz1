package com.example.controller;

import com.example.dto.AuthorazitionDTO;
import com.example.dto.AuthorazitionResponseDTO;
import com.example.dto.RegistrDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
public class AuthController {
    private Logger log= LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Operation(summary = "Create Article ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid info supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "item not found",
                    content = @Content)})
    @PostMapping("/registration")
    public ResponseEntity<?>registration(@RequestBody RegistrDTO registrDTO){
        log.info("Registration:{}"+registrDTO);

        String result=authService.registration(registrDTO);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/verification/email/{jtwToken}")
    public ResponseEntity<?> emailVerification(@PathVariable("jtwToken") String jwt) {
        String response = authService.verification(jwt);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Authorization method", description = "Method used for profile authorization")
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody AuthorazitionDTO authorazitionDTO,
                                  @RequestHeader(name = "Accept-Language",defaultValue = "ENG") Language language){
        log.info("Authorization: {}"+authorazitionDTO);

        AuthorazitionResponseDTO authorazitionResponseDTO=authService.login(authorazitionDTO, language);
        return ResponseEntity.ok(authorazitionResponseDTO);
    }


}
