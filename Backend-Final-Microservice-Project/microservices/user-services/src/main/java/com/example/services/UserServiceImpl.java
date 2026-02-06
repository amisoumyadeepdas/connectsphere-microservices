package com.example.services;

import com.example.cache.UserCacheService;
import com.example.dto.UserRequest;
import com.example.entity.User;
import com.example.kafka_producer.UserEventProducer;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCacheService cacheService;
    private final UserEventProducer eventProducer;

    @Override
    public User createUser(UserRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .city(request.getCity())
                .company(request.getCompany())
                .gender(request.getGender())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth())) // ✅ conversion
                .build();

        // 1️⃣ Save to DB (SOURCE OF TRUTH)
        User saved = userRepository.save(user);

        // 2️⃣ Save to Redis
        cacheService.save(saved);

        // 3️⃣ Publish Kafka event
        eventProducer.sendUserCreatedEvent(saved);

        return saved;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCity(request.getCity());
        user.setCompany(request.getCompany());
        user.setGender(request.getGender());

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        }

        User updated = userRepository.save(user);

        cacheService.save(updated);
        eventProducer.sendUserUpdatedEvent(updated);

        return updated;
    }

    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);

        cacheService.delete(id);
        eventProducer.sendUserDeletedEvent(id);
    }

    @Override
    public User getUserById(Long id) {

        // 1️⃣ Try Redis first
        User cachedUser = cacheService.get(id);
        if (cachedUser != null) {
            System.out.println("Cache Hit!!");
            return cachedUser;
        }

        // 2️⃣ Fallback to DB
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Store in Redis for next time
        cacheService.save(user);

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        // 1️⃣ DB is the safest for email lookup
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            log.warn("User not found in DB for email: {}", email);
            return null;
        }

        // 2️⃣ Cache by ID (not by email)
        cacheService.save(user);

        return user;
    }
}
