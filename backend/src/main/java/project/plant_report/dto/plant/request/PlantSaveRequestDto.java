package project.plant_report.dto.plant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import project.plant_report.domain.user.User;
import project.plant_report.domain.plant.Season;

import java.time.LocalDate;

@Getter
public class PlantSaveRequestDto {

    @NotBlank(message = "식물 이름은 필수입니다")
    private String name; // 식물 이름
    
    @NotNull(message = "공통 물주기 간격은 필수입니다")
    @Positive(message = "물주기 간격은 양수여야 합니다")
    private Integer commonInterval; // 공통 물주기
    
    @Positive(message = "여름 물주기 간격은 양수여야 합니다")
    private Integer summerInterval; // 여름 물주기 (선택적)
    
    @Positive(message = "겨울 물주기 간격은 양수여야 합니다")
    private Integer winterInterval; // 겨울 물주기 (선택적)
    
    private LocalDate lastWateringDate; //마지막으로 물 준 날짜 (선택적)
    
    private Season season = Season.COMMON; // 등록 시점의 계절 (기본값: COMMON)
    
    // @NotNull(message = "사용자 정보는 필수입니다") // 임시로 주석 처리
    private User user;

    public PlantSaveRequestDto(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate, Season season, User user) {
        this.name = name;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
        this.lastWateringDate = lastWateringDate;
        this.season = season;
        this.user = user;
    }
}
