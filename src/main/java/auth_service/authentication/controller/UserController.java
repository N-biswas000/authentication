package auth_service.authentication.controller;

import auth_service.authentication.dto.response.UserProfileResponseDto;
import auth_service.authentication.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public UserProfileResponseDto profile() {
        return userService.getProfile();
    }
}

