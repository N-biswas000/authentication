package auth_service.authentication.dto.response;


public class TokenRefreshResponseDto {

    private String accessToken;

    public TokenRefreshResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

}

