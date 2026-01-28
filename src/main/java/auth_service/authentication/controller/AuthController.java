package auth_service.authentication.controller;

import auth_service.authentication.dto.request.RefreshTokenRequestDto;
import auth_service.authentication.dto.request.SendOtpRequestDto;
import auth_service.authentication.dto.request.VerifyOtpRequestDto;
import auth_service.authentication.dto.response.AuthResponseDto;
import auth_service.authentication.dto.response.TokenRefreshResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import auth_service.authentication.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<AuthResponseDto> sendOtp(
            @Valid @RequestBody SendOtpRequestDto dto) {

        authService.sendOtp(dto);
        return ResponseEntity.ok(
                new AuthResponseDto("OTP sent successfully", null)
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(
            @RequestBody VerifyOtpRequestDto dto) {

        AuthResponseDto response = authService.verifyOtp(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(
            @RequestBody RefreshTokenRequestDto dto) {

        String newAccessToken =
                authService.refreshAccessToken(dto.getRefreshToken());

        return ResponseEntity.ok(
                new TokenRefreshResponseDto(newAccessToken)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshTokenRequestDto dto) {

        authService.logout(dto.getRefreshToken());
        return ResponseEntity.ok().build();
    }


}

