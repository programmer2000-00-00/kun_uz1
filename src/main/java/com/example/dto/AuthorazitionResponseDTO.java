package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorazitionResponseDTO {
    private String name;
    private String surname;
    private ProfileRole role;
    private String token;

}
