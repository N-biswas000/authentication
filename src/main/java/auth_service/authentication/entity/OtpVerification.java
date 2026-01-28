package auth_service.authentication.entity;

import auth_service.authentication.enums.OtpChannel;
import auth_service.authentication.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "otp_verification")
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identifier; // email

    @Column(nullable = false)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
