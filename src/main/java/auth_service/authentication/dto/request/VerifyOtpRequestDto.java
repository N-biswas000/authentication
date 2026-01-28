package auth_service.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyOtpRequestDto {
    @Email
    @NotBlank
    private String identifier;

    @NotBlank
    private String otp;
}
