package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String name;
    private String role;
    private String email;
    private String city;
    private String company;
    private LocalDate dateOfBirth;
    private String gender;
}