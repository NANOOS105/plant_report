package project.plant_report.dto.plant.response;

import lombok.Getter;
import project.plant_report.domain.plant.Plant;

import java.time.LocalDate;

@Getter
public class PlantResponseDto {
    private Long id;
    private String name;
    private LocalDate lastWateringDate; // 마지막 물준 날짜
    private Integer commonInterval; // 공통 물주기 간격
    private Integer summerInterval; // 여름 물주기 간격
    private Integer winterInterval; // 겨울 물주기 간격

    public PlantResponseDto(Plant plant){
        this.id = plant.getId();
        this.name = plant.getName();
        this.lastWateringDate = plant.getLastWateringDate();
        this.commonInterval = plant.getCommonInterval();
        this.summerInterval = plant.getSummerInterval();
        this.winterInterval = plant.getWinterInterval();
    }
}
