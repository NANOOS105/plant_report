package project.plant_report.dto.plant.response;

import lombok.Getter;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.Season;

@Getter
public class PlantResponseDto {
    private Long id;
    private String name;
    private int wateringInterval; // 현재 계절에 맞는 물주기 간격
    private String nextWateringDate; // 다음 물주기 날짜
    private Season season;

    public PlantResponseDto(Plant plant, int wateringInterval, String nextWateringDate, Season season) {
        this.id = plant.getId();
        this.name = plant.getName();
        this.wateringInterval = wateringInterval;
        this.nextWateringDate = nextWateringDate;
        this.season = season;
    }
}
