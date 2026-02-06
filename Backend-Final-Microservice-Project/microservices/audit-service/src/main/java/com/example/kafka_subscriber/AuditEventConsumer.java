package com.example.kafka_subscriber;

import com.example.entity.AuditLog;
import com.example.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "user-events", groupId = "audit-group-v3")
    public void consume(ConsumerRecord<String, String> record) {

        String eventType = record.key();      // USER_CREATED, USER_LOGGED_IN, etc
        Object payload = record.value();      // User / LoginResponse / Long

        System.out.println("📌 EVENT RECEIVED");
        System.out.println("Type: " + eventType);
        System.out.println("Data: " + payload);

        AuditLog log = AuditLog.builder()
                .eventType(eventType)
                .payload(payload.toString())
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}

