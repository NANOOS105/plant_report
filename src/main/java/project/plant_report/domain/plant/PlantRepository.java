package project.plant_report.domain.plant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByIsWateringRequired(Boolean isWateringRequired);
    Page<Plant> findByNextWaterAtLessThanEqual(LocalDate date, Pageable pageable);

}
