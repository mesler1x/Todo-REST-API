package com.roslabsystem.todo.configuration;

import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.domain.user.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserInitializerConfig implements CommandLineRunner {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String encodedPass = passwordEncoder.encode("sample");
        UserEntity user = UserEntity.from(new UserEntity.Context("sample", encodedPass));
        userRepository.save(user);
    }
}
