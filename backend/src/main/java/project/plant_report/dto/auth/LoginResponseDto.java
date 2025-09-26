package project.plant_report.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String email;
    private String name;
    private Long id;

    public LoginResponseDto(String token, String email, String name, Long id) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.id = id;
    }
}
