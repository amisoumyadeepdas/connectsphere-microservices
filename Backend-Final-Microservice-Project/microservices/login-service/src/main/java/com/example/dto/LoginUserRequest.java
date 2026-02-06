package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {

    private String name;
    private String email;
    private String city;
    private String company;
    private String gender;

    // yyyy-MM-dd
    private String dateOfBirth;
}
