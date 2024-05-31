package com.roslabsystem.todo.service;

import com.roslabsystem.todo.adapter.repository.UserRepository;
import com.roslabsystem.todo.adapter.web.dto.RegistrationRequest;
import com.roslabsystem.todo.adapter.web.dto.RegistrationResponse;
import com.roslabsystem.todo.adapter.web.exceptions.AlreadyExistException;
import com.roslabsystem.todo.adapter.web.exceptions.NotFoundException;
import com.roslabsystem.todo.domain.user.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> person = userRepository.findByUsername(username);

        if (person.isEmpty())
            throw new NotFoundException(username);

        return person.get();
    }

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        String encodePass = passwordEncoder.encode(registrationRequest.password());
        Optional<UserEntity> checkUsername = userRepository.findByUsername(registrationRequest.username());
        if (checkUsername.isPresent()) {
            throw new AlreadyExistException("User");
        }

        UserEntity.Context context = new UserEntity.Context(
                registrationRequest.username(),
                encodePass);

        UserEntity user = UserEntity.from(context);
        userRepository.save(user);

        List<String> userRoles = user.getAuthorities().stream().map(Enum::name).toList();
        return new RegistrationResponse(user.getUsername(), user.getPassword(), userRoles);
    }
}
