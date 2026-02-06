package com.example.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long id;
    private String email;
    private String role;

    // Optional profile data
    private String name;
    private String city;
    private String company;
    private String gender;
    private LocalDate dateOfBirth;
}

