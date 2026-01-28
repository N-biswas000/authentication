package auth_service.authentication.repository;

import auth_service.authentication.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification>
    findTopByIdentifierAndIsUsedFalseOrderByCreatedAtDesc(String identifier);
}
