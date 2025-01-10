package project.plant_report.dto.plant.request;

import lombok.Getter;

@Getter
public class PlantRequestDto {
    private String name; // 식물 이름
    private int wateringInterval; // 물주기 간격
}
