package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
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
    private String notes; // 메모

    //== 생성자 ==
    protected Plant(){}

    public Plant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval, LocalDate lastWateringDate, Season season, User user, String notes) {
        this.name = name;
        this.user = user;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
        this.notes = notes;

        // 초기 물주기 상태 설정
        if (lastWateringDate != null) {
            this.lastWateringDate = lastWateringDate;
            
            // 초기 물주기 기록 생성 (계절 정보 포함)
            WateringRecord initialRecord = new WateringRecord(this, lastWateringDate, season);
            this.wateringRecords.add(initialRecord);
        } else {
            this.lastWateringDate = null;
        }
    }

    // 테스트 및 편의용 생성자
    public Plant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval) {
        this(name, commonInterval, summerInterval, winterInterval, null, Season.COMMON, null, null);
    }

    //== 비즈니스 로직 ==

    // 식물 정보 수정
    public void updatePlant(String name, Integer commonInterval, Integer summerInterval, Integer winterInterval, String notes) {
        this.name = name;
        this.commonInterval = commonInterval;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
        this.notes = notes;
    }

    // 물주기 실행
    public void waterPlant(Season season) {
        LocalDate today = LocalDate.now();
        this.lastWateringDate = today;
        
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
        this.wateringRecords.remove(this.wateringRecords.size() - 1);
        
        // 이전 상태로 복원
        if (this.wateringRecords.isEmpty()) {
            this.lastWateringDate = null;
        } else {
            // 이전 기록으로 상태 복원
            WateringRecord previousRecord = this.wateringRecords.get(this.wateringRecords.size() - 1);
            this.lastWateringDate = previousRecord.getLastWateringDate();
        }
    }

    // 마지막 물주기 날짜 설정
    public void setLastWateringDate(LocalDate lastWateringDate) {
        this.lastWateringDate = lastWateringDate;
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

}