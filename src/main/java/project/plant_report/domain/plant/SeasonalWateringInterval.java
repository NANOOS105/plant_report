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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Season season; // 여름, 겨울 (ENUM)

    @Column(nullable = false)
    private int interval; // 계절별 물주기 간격 (일 단위)

    public SeasonalWateringInterval(Plant plant, Season season, Integer seasonInterval) {
        this.plant = plant;
        this.season = season;
        this.interval = seasonInterval;

    }
}

