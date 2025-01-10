package project.plant_report.domain.plant;

import jakarta.persistence.*;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

@Entity
public class Plant extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nName;

    @Enumerated(EnumType.STRING) // ENUM 매핑 방식: String 사용
    @Column(nullable = false)
    private Season seoson;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
