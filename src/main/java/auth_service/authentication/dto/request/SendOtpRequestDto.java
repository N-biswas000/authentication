package auth_service.authentication.dto.request;

import auth_service.authentication.enums.OtpChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOtpRequestDto {
    @Email
    @NotBlank
    private String identifier; // email for now

    private OtpChannel channel; // EMAIL (default)
}
