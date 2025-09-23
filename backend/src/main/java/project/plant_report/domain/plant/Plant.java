package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
    @Index(name = "idx_next_watering_date", columnList = "nextWateringDate")
})
@Getter
public class Plant extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 식물 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WateringRecord> wateringRecords = new ArrayList<>();

    // 물주기 간격 (단순화)
    @Column(nullable = false)
    private Integer commonInterval; // 공통 물주기 간격 (일)

    private Integer summerInterval; // 여름 물주기 간격 (일, 선택적)
    private Integer winterInterval; // 겨울 물주기 간격 (일, 선택적)

    private LocalDate lastWateringDate; // 마지막 물 준 날짜
    private LocalDate nextWateringDate; // 다음 물주기 예정 날짜
    private Boolean isWateringRequired = false; // 물주기 필요 여부

    //== 생성자 ==
    protected Plant(){}

    public Plant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate, User user) {
        this.name = name;
        this.user = user;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;

        // 초기 물주기 상태 설정
        if (lastWateringDate != null) {
            this.lastWateringDate = lastWateringDate;
            this.nextWateringDate = lastWateringDate.plusDays(commonInterval);
            this.isWateringRequired = LocalDate.now().isAfter(this.nextWateringDate);
        } else {
            this.lastWateringDate = null;
            this.nextWateringDate = null;
            this.isWateringRequired = false;
        }
    }

    // 테스트 및 편의용 생성자
    public Plant(String name, int commonInterval, int summerInterval, Integer winterInterval) {
        this(name, Integer.valueOf(commonInterval), Integer.valueOf(summerInterval), winterInterval, null, null);
    }

    //== 비즈니스 로직 ==

    // 식물 정보 수정
    public void updatePlant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval) {
        this.name = name;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
        
        // 다음 물주기 날짜 재계산
        if (this.lastWateringDate != null) {
            this.nextWateringDate = this.lastWateringDate.plusDays(commonInterval);
            this.isWateringRequired = LocalDate.now().isAfter(this.nextWateringDate);
        }
    }

    // 물주기 실행
    public void waterPlant(Season season) {
        LocalDate today = LocalDate.now();
        this.lastWateringDate = today;
        
        // 계절에 따른 간격 선택
        Integer interval = getIntervalForSeason(season);
        this.nextWateringDate = today.plusDays(interval);
        this.isWateringRequired = false;
        
        // 물주기 기록 추가
        WateringRecord record = new WateringRecord(this, today, season);
        this.wateringRecords.add(record);
    }

    // 마지막 물주기 취소
    public void cancelLastWaterPlant(Season season) {
        if (this.wateringRecords.isEmpty()) {
            throw new IllegalStateException("취소할 물주기 기록이 없습니다.");
        }
        
        // 마지막 기록 제거
        WateringRecord lastRecord = this.wateringRecords.remove(this.wateringRecords.size() - 1);
        
        // 이전 상태로 복원
        if (this.wateringRecords.isEmpty()) {
            this.lastWateringDate = null;
            this.nextWateringDate = null;
            this.isWateringRequired = false;
        } else {
            // 이전 기록으로 상태 복원
            WateringRecord previousRecord = this.wateringRecords.get(this.wateringRecords.size() - 1);
            this.lastWateringDate = previousRecord.getLastWateringDate();
            
            Integer interval = getIntervalForSeason(previousRecord.getSeason());
            this.nextWateringDate = this.lastWateringDate.plusDays(interval);
            this.isWateringRequired = LocalDate.now().isAfter(this.nextWateringDate);
        }
    }

    // 계절에 따른 물주기 간격 반환
    public Integer getIntervalForSeason(Season season) {
        switch (season) {
            case SUMMER:
                return summerInterval != null ? summerInterval : commonInterval;
            case WINTER:
                return winterInterval != null ? winterInterval : commonInterval;
            case COMMON:
            default:
                return commonInterval;
        }
    }

    // 물주기 필요 여부 업데이트
    public void updateWateringStatus() {
        if (this.nextWateringDate != null) {
            this.isWateringRequired = LocalDate.now().isAfter(this.nextWateringDate);
        }
    }
}