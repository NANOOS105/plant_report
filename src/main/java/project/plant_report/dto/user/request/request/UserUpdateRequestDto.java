package project.plant_report.dto.user.request.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserUpdateRequestDto {
    private Long id;
    private String name;
    private String password;

    public UserUpdateRequestDto(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
