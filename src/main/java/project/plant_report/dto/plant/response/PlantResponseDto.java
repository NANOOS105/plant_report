package project.plant_report.dto.plant.response;

import lombok.Getter;

@Getter
public class PlantResponseDto {
    private Long id;
    private String name;
    private int wateringInterval;
    private String nextWateringDate;
}
