package project.plant_report.dto.plant.request;

import lombok.Getter;

@Getter
public class PlantSaveRequestDto {
    private String name; // 식물 이름
    private int commonInterval; // 공통 물주기
    private int summerInterval; // 여름 물주기 (선택적)
    private int winterInterval; // 겨울 물주기 (선택적)
}
