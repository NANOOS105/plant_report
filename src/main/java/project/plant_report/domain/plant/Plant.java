package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

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
    private int commonInterval;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonalWateringInterval> seasonalIntervals = new ArrayList<>(); // 계절별 물주기

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WateringRecord> wateringRecords = new ArrayList<>();

    protected Plant(){}

    public Plant(String name, int commonInterval, Integer summerInterval, Integer winterInterval) {
        this.name = name;
        this.commonInterval = commonInterval;

        // 여름 데이터가 있는 경우만 저장
        if (summerInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.SUMMER, summerInterval));
        }

        // 겨울 데이터가 있는 경우만 저장
        if (winterInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.WINTER, winterInterval));
        }
    }

    //====비즈니스 로직=====
    // 특정 계절의 물주기 조회
    public int getIntervalForSeason(Season season) {
        return this.seasonalIntervals.stream()
                .filter(interval -> interval.getSeason() == season)
                .map(SeasonalWateringInterval::getInterval)
                .findFirst()
                .orElse(this.commonInterval); // 값이 없으면 공통 물주기 반환
    }



}
