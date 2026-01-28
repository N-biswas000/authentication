package auth_service.authentication.repository;

import auth_service.authentication.entity.User;
import auth_service.authentication.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    long countByRole(Role role);
}
