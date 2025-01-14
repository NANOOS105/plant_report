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

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Enumerated(EnumType.STRING)
    private Season season;

    private LocalDate lastWateringDate;

    private LocalDate nextWateringDate;

    protected WateringRecord(){}
    public WateringRecord(Plant plant, LocalDate lastWateringDate, LocalDate nextWateringDate, Season season) {
        this.plant = plant;
        this.lastWateringDate = lastWateringDate;
        this.nextWateringDate = nextWateringDate;
        this.season = season;
    }

}
