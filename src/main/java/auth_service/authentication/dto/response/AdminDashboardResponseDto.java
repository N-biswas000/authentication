package auth_service.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDashboardResponseDto {

    private long totalUsers;
    private long totalAdmins;

}

