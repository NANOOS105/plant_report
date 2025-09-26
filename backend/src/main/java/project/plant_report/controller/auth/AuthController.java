package project.plant_report.controller.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.plant_report.dto.auth.LoginRequestDto;
import project.plant_report.dto.auth.LoginResponseDto;
import project.plant_report.dto.user.request.UserSaveRequestDto;
import project.plant_report.service.auth.AuthService;
import project.plant_report.service.user.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserSaveRequestDto registerRequest) {
        userService.saveUser(registerRequest);
        return ResponseEntity.ok().build();
    }
}
