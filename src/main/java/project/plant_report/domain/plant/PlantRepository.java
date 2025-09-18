package project.plant_report.domain.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByIsWateringRequired(Boolean isWateringRequired);
    Page<Plant> findByNextWateringDateLessThanEqual(LocalDate date, Pageable pageable);

}
