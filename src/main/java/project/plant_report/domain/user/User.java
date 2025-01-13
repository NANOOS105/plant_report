package project.plant_report.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.plant.Plant;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plant> plants = new ArrayList<>();

    protected User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;;
        this.password = password;
    }

    // == 비즈니스 로직 ==
    public void updateUser(String name, String password) {
        if(name != null & !name.isBlank()){
            this.name = name;
        }
        if(password != null & !password.isBlank()){
            this.password = password;
        }

    }
}
