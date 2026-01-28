package auth_service.authentication.service;

import auth_service.authentication.dto.request.SendOtpRequestDto;
import auth_service.authentication.dto.request.VerifyOtpRequestDto;
import auth_service.authentication.dto.response.AuthResponseDto;
import auth_service.authentication.entity.OtpVerification;
import auth_service.authentication.entity.RefreshToken;
import auth_service.authentication.entity.User;
import auth_service.authentication.enums.OtpPurpose;
import auth_service.authentication.enums.Role;
import auth_service.authentication.repository.RefreshTokenRepository;
import auth_service.authentication.security.JwtUtil;
import org.springframework.stereotype.Service;
import auth_service.authentication.repository.OtpRepository;
import auth_service.authentication.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final OtpRepository otpRepository;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository,
                       OtpRepository otpRepository,
                       EmailService emailService,
                       JwtUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // SEND OTP
    public void sendOtp(SendOtpRequestDto dto) {

        String otp = generateOtp();

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setIdentifier(dto.getIdentifier());
        otpEntity.setOtpCode(otp);
        otpEntity.setChannel(dto.getChannel());
        otpEntity.setPurpose(OtpPurpose.LOGIN);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpEntity.setIsUsed(false);
        otpEntity.setCreatedAt(LocalDateTime.now());

        otpRepository.save(otpEntity);

        emailService.sendOtp(dto.getIdentifier(), otp);
    }

    // VERIFY OTP (Signup + Login)
    public AuthResponseDto verifyOtp(VerifyOtpRequestDto dto) {

        OtpVerification otpEntity = otpRepository
                .findTopByIdentifierAndIsUsedFalseOrderByCreatedAtDesc(dto.getIdentifier())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        validateOtp(otpEntity, dto.getOtp());

        otpEntity.setIsUsed(true);
        otpRepository.save(otpEntity);

        User user = userRepository.findByEmail(dto.getIdentifier())
                .orElseGet(() -> createNewUser(dto.getIdentifier()));

        String accessToken =
                jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());

        String refreshToken =
                jwtUtil.generateRefreshToken(user.getEmail());

        saveRefreshToken(user, refreshToken);

        return new AuthResponseDto(accessToken, refreshToken);

    }

    private void saveRefreshToken(User user, String token) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        refreshTokenRepository.save(refreshToken);
    }


    private void validateOtp(OtpVerification otpEntity, String otp) {

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (!otpEntity.getOtpCode().equals(otp))
            throw new RuntimeException("Invalid OTP");
    }

    private User createNewUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setEmailVerified(true);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public String refreshAccessToken(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository
                .findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = tokenEntity.getUser();

        return jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getRole().name()
        );
    }

    public void logout(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);
    }



    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}

