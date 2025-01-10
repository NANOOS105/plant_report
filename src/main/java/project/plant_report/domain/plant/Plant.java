package project.plant_report.domain.plant;

import jakarta.persistence.*;
import project.plant_report.domain.user.User;

import java.time.Instant;

@Entity
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private int id;

    @Column(name = "plant_name", nullable = false)
    private String nName;

    @Enumerated(EnumType.STRING) // ENUM 매핑 방식: String 사용
    @Column(name = "plant_type", nullable = false)
    private PlantType type;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
