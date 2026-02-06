package com.example.kafka_producer;

import com.example.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreatedEvent(User user) {

        kafkaTemplate.send("user-events", "USER_CREATED", user);
    }

    public void sendUserUpdatedEvent(User user) {

        kafkaTemplate.send("user-events", "USER_UPDATED", user);
    }

    public void sendUserDeletedEvent(Long userId) {

        kafkaTemplate.send("user-events", "USER_DELETED", userId);
    }
}

