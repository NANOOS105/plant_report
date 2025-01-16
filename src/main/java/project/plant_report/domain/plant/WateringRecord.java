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

    private String note;

    protected WateringRecord(){}

    public WateringRecord(Plant plant, LocalDate lastWateringDate, String note) {
        this.plant = plant;
        this.lastWateringDate = lastWateringDate;
        this.note = note;
    }
}
