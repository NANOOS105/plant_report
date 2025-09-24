package project.plant_report.domain.plant;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class WateringRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private LocalDate lastWateringDate;

    @Enumerated(EnumType.STRING)
    private Season season;

    protected WateringRecord(){}

    public WateringRecord(Plant plant, LocalDate lastWateringDate, Season season) {
        this.plant = plant;
        this.lastWateringDate = lastWateringDate;
        this.season = season;
    }
}
