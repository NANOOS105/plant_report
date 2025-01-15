package project.plant_report.dto.plant.request;

import lombok.Getter;
import project.plant_report.domain.user.User;

import java.time.LocalDate;

@Getter
public class PlantSaveRequestDto {

    private String name; // 식물 이름
    private int commonInterval; // 공통 물주기
    private Integer summerInterval; // 여름 물주기 (선택적)
    private Integer winterInterval; // 겨울 물주기 (선택적)
    private LocalDate lastWateringDate; //마지막으로 물 준 날짜 (선택적)
    private User user;

    public PlantSaveRequestDto(String name, int commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate, User user) {
        this.name = name;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
        this.lastWateringDate = lastWateringDate;
        this.user = user;
    }
}
