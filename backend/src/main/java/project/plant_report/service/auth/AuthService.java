package project.plant_report.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.plant_report.config.JwtUtil;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.auth.LoginRequestDto;
import project.plant_report.dto.auth.LoginResponseDto;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 로그인 처리
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // 4. 응답 데이터 생성
        return new LoginResponseDto(token, user.getEmail(), user.getName(), user.getId());
    }
}
