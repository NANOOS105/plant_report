package project.plant_report.dto.plant.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PlantUpdateRequestDto {

    // 업데이트는 부분 변경을 허용하므로 모두 선택 입력으로 둔다
    private String name; // 식물 이름 (선택)

    @Positive(message = "공통 물주기 간격은 양수여야 합니다")
    private Integer commonInterval; // 공통 물주기 (선택)

    @Positive(message = "여름 물주기 간격은 양수여야 합니다")
    private Integer summerInterval; // 여름 물주기 (선택)

    @Positive(message = "겨울 물주기 간격은 양수여야 합니다")
    private Integer winterInterval; // 겨울 물주기 (선택)
}
