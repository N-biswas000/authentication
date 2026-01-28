package auth_service.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileResponseDto {

    private String email;
    private String role;
    private LocalDateTime joinedAt;

}

