package project.plant_report.domain.plant;

import jakarta.persistence.*;
import project.plant_report.domain.user.User;

import java.time.Instant;

@Entity
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nName;

    @Enumerated(EnumType.STRING) // ENUM 매핑 방식: String 사용
    @Column(nullable = false)
    private PlantType type;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
