package project.plant_report.dto.plant.request;

import lombok.Getter;
import project.plant_report.domain.user.User;

import java.time.LocalDate;

@Getter
public class PlantSaveRequestDto {

    private User user;
    private String name; // 식물 이름
    private int commonInterval; // 공통 물주기
    private int summerInterval; // 여름 물주기 (선택적)
    private int winterInterval; // 겨울 물주기 (선택적)
    private LocalDate lastWateringDate; //마지막으로 물 준 날짜 (선택적)
}
