package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SeasonalWateringInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant; // 특정 식물과 연결

    @Column
    private Season season;

    @Column
    private Integer wateringInterval;


    protected SeasonalWateringInterval() {}

    public SeasonalWateringInterval(Plant plant, Season season, Integer wateringInterval) {
        this.plant = plant;
        this.season = season;
        this.wateringInterval = wateringInterval;
    }

    public void updateInterval(int wateringInterval) {
        this.wateringInterval = wateringInterval;
    }
}

