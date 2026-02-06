package com.example.kafka_producer;

import com.example.dto.LoginResponse;
//import com.example.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginEventProducer {

    private final KafkaTemplate<String, LoginResponse> kafkaTemplate;

    public void userLoggedIn(LoginResponse user) {
        kafkaTemplate.send("user-events", "USER_LOGGED_IN", user);
    }

    public void adminLoggedIn(LoginResponse admin) {
        kafkaTemplate.send("user-events", "ADMIN_LOGGED_IN", admin);
    }

    public void userRegistered(LoginResponse user) {
        kafkaTemplate.send("user-events", "USER_REGISTERED", user);
    }
}





