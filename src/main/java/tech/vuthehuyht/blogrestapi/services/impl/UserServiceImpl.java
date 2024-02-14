package tech.vuthehuyht.blogrestapi.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.vuthehuyht.blogrestapi.constants.Constants;
import tech.vuthehuyht.blogrestapi.dto.request.UserRequest;
import tech.vuthehuyht.blogrestapi.dto.response.ResponseErrorTemplate;
import tech.vuthehuyht.blogrestapi.dto.response.UserResponse;
import tech.vuthehuyht.blogrestapi.exceptions.CustomMessageException;
import tech.vuthehuyht.blogrestapi.models.Role;
import tech.vuthehuyht.blogrestapi.models.User;
import tech.vuthehuyht.blogrestapi.repositories.RoleRepository;
import tech.vuthehuyht.blogrestapi.repositories.UserRepository;
import tech.vuthehuyht.blogrestapi.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseErrorTemplate createUser(UserRequest request) {
        this.userRequestValidation(request);

        List<Role> roles = roleRepository.findFirstByName("USER").stream().toList();

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roles(roles)
                .loginAttempt(0)
                .status(Constants.ACTIVE)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return this.userMapper(user);
    }

    // validate user request
    private void userRequestValidation(UserRequest request) {
        Optional<User> user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (user.isPresent()) {
            log.warn("username or email can not be duplicate");
            throw new CustomMessageException(
                    "username or email can not be duplicate",
                    String.valueOf(HttpStatus.BAD_REQUEST.value())
            );
        }
    }

    @Override
    public ResponseErrorTemplate findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        var msg = String.format("User id %s not found", id);
        return userOptional.map(this::userMapper)
                .orElse(new ResponseErrorTemplate(msg, String.valueOf(HttpStatus.NOT_FOUND.value()), new Object()));
    }

    @Override
    public ResponseErrorTemplate findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        var msg = String.format("User username %s not found", username);
        return userOptional.map(this::userMapper)
                .orElse(new ResponseErrorTemplate(msg, String.valueOf(HttpStatus.NOT_FOUND.value()), new Object()));
    }

    private ResponseErrorTemplate userMapper(User user) {
        UserResponse response = new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles().stream().map(Role::getName).toList(),
                user.getLoginAttempt()
        );
        return new ResponseErrorTemplate(Constants.SUC_MSG, Constants.SUC_CODE, response);
    }
}
