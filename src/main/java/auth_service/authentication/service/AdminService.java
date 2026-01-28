package auth_service.authentication.service;

import auth_service.authentication.dto.response.AdminDashboardResponseDto;
import auth_service.authentication.enums.Role;
import auth_service.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminDashboardResponseDto dashboard() {

        long totalUsers = userRepository.count();
        long admins = userRepository.countByRole(Role.ADMIN);

        return new AdminDashboardResponseDto(totalUsers, admins);
    }
}

