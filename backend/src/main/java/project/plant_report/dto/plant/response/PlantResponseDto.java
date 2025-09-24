package project.plant_report.dto.plant.response;

import lombok.Getter;
import project.plant_report.domain.plant.Plant;

import java.time.LocalDate;

@Getter
public class PlantResponseDto {
    private Long id;
    private String name;
    private LocalDate lastWateringDate; // 마지막 물준 날짜
    private LocalDate nextWateringDate; // 다음 물주기 날짜
    private Boolean isWateringRequired;

    public PlantResponseDto(Plant plant){

        this.id = plant.getId();
        this.name = plant.getName();
        this.lastWateringDate = plant.getLastWateringDate();
        this.nextWateringDate = plant.getNextWateringDate();
        this.isWateringRequired = plant.getIsWateringRequired();
    }
}
