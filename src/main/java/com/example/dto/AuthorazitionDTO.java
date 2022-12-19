package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorazitionDTO {
    private Integer id;
    private String phone;
    private String password;

    public AuthorazitionDTO(Integer id, String phone, String password) {
        this.id = id;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthorazitionDTO{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
