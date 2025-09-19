package project.plant_report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlantReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantReportApplication.class, args);
    }

}
