package com.example.controller;

import com.example.entity.AuditLog;
import com.example.kafka_subscriber.AuditEventConsumer;
import com.example.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping("/auditLog")
    public ResponseEntity<List<AuditLog>> getAuditLog() {
        return ResponseEntity.ok(auditLogRepository.findAll());
    }
}

