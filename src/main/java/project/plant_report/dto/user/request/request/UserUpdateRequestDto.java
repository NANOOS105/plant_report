package project.plant_report.dto.user.request.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private Long id;
    private String name;
    private String password;
}
