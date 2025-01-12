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
    private Integer summerInterval;

    @Column
    private Integer winterInterval;

    public SeasonalWateringInterval(Plant plant, Integer summerInterval, Integer winterInterval) {
        this.plant = plant;
        this.summerInterval = summerInterval;
        this.winterInterval = winterInterval;
    }
}

