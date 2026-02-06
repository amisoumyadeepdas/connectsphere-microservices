package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
        private String name;
        private String city;
        private String email;
        private String company;
        private String dateOfBirth; // String
        private String gender;
}

