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

        // 계절별 물주기를 추가 (여름, 겨울)
        if (summerInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.SUMMER, summerInterval));
        }
        if (winterInterval != null) {
            this.seasonalIntervals.add(new SeasonalWateringInterval(this, Season.WINTER, winterInterval));
        }
    }
}
