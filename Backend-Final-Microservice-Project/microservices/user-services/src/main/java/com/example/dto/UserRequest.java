package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String name;
    private String email;
    private String city;
    private String company;
    private String gender;

    // Frontend sends "yyyy-MM-dd"
    private String dateOfBirth;
}

