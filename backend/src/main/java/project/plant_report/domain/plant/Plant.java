package project.plant_report.domain.plant;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "idxNextWateringDate", columnList = "nextWateringDate")
})
@Getter
public class Plant extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isWateringRequired  = false; // 물 줬으면 true , 물 안줬으면 false

    private LocalDate lastWateringDate;
    @Column
    private LocalDate nextWateringDate;

    //계절이 늘어날 수도 있는 것을 고려하여
    //테이블로 따로 빼서 관리
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonalWateringInterval> seasonalIntervals = new ArrayList<>(); // 계절별 물주기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WateringRecord> wateringRecords = new ArrayList<>();

    //== 생성자 ==
    protected Plant(){}

    public Plant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate,User user) {
        this.name = name;
        this.user = user;

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

        // 초기 물주기 상태 설정
        if (lastWateringDate != null) {
            this.lastWateringDate = lastWateringDate;
            this.nextWateringDate = lastWateringDate.plusDays(commonInterval);
            this.isWateringRequired = lastWateringDate.isAfter(this.nextWateringDate);
        }

    }

    // 테스트 및 편의용 생성자 (기존 테스트 시그니처 호환)
    public Plant(String name, int commonInterval, int summerInterval, Integer winterInterval) {
        this(name, Integer.valueOf(commonInterval), Integer.valueOf(summerInterval), winterInterval, null, null);
    }

    //====비즈니스 로직=====
    // 식물 업데이트
    // 업데이트는 엔터티의 상태를 변경하는 작업이기 때문에
    // 엔터티 내부에서 캡슐화해서 처리
    public void updatePlant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval) {

        // 이름 변경
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        // 공통 물주기 간격 변경
        if (commonInterval != null) {
            updateSeasonalInterval(Season.COMMON, commonInterval);
        }
        // 계절별 물주기 변경
        if (summerInterval != null) {
            updateSeasonalInterval(Season.SUMMER, summerInterval);
        }
        if (winterInterval != null) {
            updateSeasonalInterval(Season.WINTER, winterInterval);
        }
    }

    //계절에 따라 물주기 변경 로직
    //데이터가 있으면 업데이트, 없을 때만 새로 생성
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
        LocalDate today = LocalDate.now();

        // 마지막 물준 날짜와 다음 물주기 날짜 갱신
        this.lastWateringDate = today;
        int interval = findIntervalBySeason(season).getWateringInterval();
        this.nextWateringDate = today.plusDays(interval);

        // 물 준 기록 추가
        this.wateringRecords.add(new WateringRecord(this, today, null));

        // 물 준 상태 변경
        this.isWateringRequired = true;
    }

    // 물주기 취소 로직
    public void cancelLastWaterPlant(Season season) {

        int interval = findIntervalBySeason(season).getWateringInterval();

        if (wateringRecords.isEmpty()) {
            throw new IllegalStateException("No watering records to cancel.");
        }

        // 가장 최근 기록 제거
        WateringRecord lastRecord = wateringRecords.remove(wateringRecords.size() - 1);

        // 물 준 상태 업데이트
        if (wateringRecords.isEmpty()) {
            this.isWateringRequired  = false;
        } else {
            this.lastWateringDate = wateringRecords.get(wateringRecords.size() - 1).getLastWateringDate();
            this.nextWateringDate = lastWateringDate.plusDays(interval);
            this.isWateringRequired  = lastWateringDate.isBefore(nextWateringDate);
        }
    }

    //계절별 물주기 찾기 로직
    public SeasonalWateringInterval findIntervalBySeason(Season season) {
        return seasonalIntervals.stream()
                .filter(interval -> interval.getSeason() == season)
                .findFirst()
                .orElseGet(() -> {
                    // 해당 계절이 없으면 COMMON 계절을 기본값으로 사용
                    return seasonalIntervals.stream()
                            .filter(interval -> interval.getSeason() == Season.COMMON)
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("COMMON season not found"));
                });
    }

    // 테스트 호환용: 특정 계절의 물주기 간격 반환
    public int getIntervalForSeason(Season season) {
        return findIntervalBySeason(season).getWateringInterval();
    }

    //다음 물주기 날짜 찾는 로직
    public LocalDate calNextWateringdate(Plant plant ,Season season){
        SeasonalWateringInterval interval = plant.findIntervalBySeason(season);
        LocalDate today = LocalDate.now();
        int wateringInterval = interval.getWateringInterval();
        LocalDate nextWateringDate = today.plusDays(wateringInterval);
        return  nextWateringDate;
    }
}

