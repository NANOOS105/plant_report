package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Plant extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isWateringRequired  = false; // 물 줬는지 여부

    @Column(nullable = false)
    private int commonInterval;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonalWateringInterval> seasonalIntervals = new ArrayList<>(); // 계절별 물주기

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WateringRecord> wateringRecords = new ArrayList<>();

    //== 생성자 ==
    protected Plant(){}

    public Plant(String name, int commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate) {
        this.name = name;

        // 공통 물주기 추가
        this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.COMMON, commonInterval));
        // 여름 물주기 추가
        if (summerInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.SUMMER, summerInterval));
        }
        // 겨울 물주기 추가
        if (winterInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.WINTER, winterInterval));
        }

        // 공통 물주기를 이용한 nextWateringDate 기록 추가
        // null값으로 들어올 경우에는 null로 저장
        WateringRecord record;
        if (lastWateringDate != null) {
            LocalDate nextWateringDate = lastWateringDate.plusDays(commonInterval);
            record = new WateringRecord(this, lastWateringDate, nextWateringDate, Season.COMMON);
            this.isWateringRequired = lastWateringDate.plusDays(commonInterval).isBefore(LocalDate.now());
        } else {
            record= new WateringRecord(this, lastWateringDate, null, Season.COMMON);
            this.isWateringRequired = false;
        }

        this.wateringRecords.add(record);

    }

    //====비즈니스 로직=====
    // 식물 업데이트
    // 업데이트는 엔터티의 상태를 변경하는 작업이기 때문에
    // 엔터티 내부에서 캡슐화해서 처리
    public void updatePlant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval) {
        if (name != null && !name.isBlank()) {
            this.name = name; // 이름 변경
        }
        if (commonInterval != null) {
            this.commonInterval = commonInterval; // 공통 물주기 간격 변경
        }

        // 계절별 물주기 변경
        if (summerInterval != null) {
            updateSeasonalInterval(Season.SUMMER, summerInterval);
        }
        if (winterInterval != null) {
            updateSeasonalInterval(Season.WINTER, winterInterval);
        }
    }

    private void updateSeasonalInterval(Season season, int wateringInterval) {
        this.seasonalIntervals.stream()
                .filter(seasonalInterval -> seasonalInterval.getSeason() == season) // 계절 필터
                .findFirst()
                .ifPresentOrElse(
                        seasonalInterval -> seasonalInterval.updateInterval(wateringInterval), // 있으면 업데이트
                        () -> this.seasonalIntervals.add(new SeasonalWateringInterval(this, season, wateringInterval)) // 없으면 새로 추가
                );
    }

    // 물주기 버튼 로직
    public void waterPlant(Season season) {
        SeasonalWateringInterval interval = findIntervalBySeason(season);
        LocalDate today = LocalDate.now();
        int wateringInterval = interval.getWateringInterval();

        // 다음 물 주는 날짜 계산
        LocalDate nextWateringDate = today.plusDays(wateringInterval);

        // 기록 추가
        WateringRecord record = new WateringRecord(this, today, nextWateringDate, season);
        this.wateringRecords.add(record);

        // 물 준 상태 업데이트
        this.isWateringRequired  = true;
    }

    private SeasonalWateringInterval findIntervalBySeason(Season season) {
        return seasonalIntervals.stream()
                .filter(interval -> interval.getSeason() == season)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Season not found: " + season));
    }

    // 물주기 취소 로직
    public void cancelLastWaterPlant() {
        if (wateringRecords.isEmpty()) {
            throw new IllegalStateException("No watering records to cancel.");
        }

        // 가장 최근 기록 제거
        WateringRecord lastRecord = wateringRecords.remove(wateringRecords.size() - 1);

        // 물 준 상태 업데이트
        if (wateringRecords.isEmpty()) {
            this.isWateringRequired  = false;
        } else {
            LocalDate lastWateringDate = wateringRecords.get(wateringRecords.size() - 1).getLastWateringDate();
            LocalDate nextWateringDate = wateringRecords.get(wateringRecords.size() - 1).getNextWateringDate();
            this.isWateringRequired  = lastWateringDate.isBefore(nextWateringDate);
        }
    }

}

