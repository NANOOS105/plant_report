package project.plant_report.dto.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {
    private String name;
    private String email;
    private String password;

    public UserSaveRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }
}
