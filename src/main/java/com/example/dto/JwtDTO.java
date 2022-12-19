package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {
    private String phone;
    private ProfileRole role;

    public JwtDTO(String phone, ProfileRole role) {
        this.phone = phone;
        this.role = role;
    }
}
