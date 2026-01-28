package auth_service.authentication.service;

import auth_service.authentication.dto.response.UserProfileResponseDto;
import auth_service.authentication.entity.User;
import auth_service.authentication.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponseDto getProfile() {

        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponseDto(
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}

