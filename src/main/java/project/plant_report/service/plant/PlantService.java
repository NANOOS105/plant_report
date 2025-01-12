package project.plant_report.service.plant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.dto.plant.request.PlantRequestDto;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    //식물 등록 서비스
    @Transactional
    public void savePlant(PlantRequestDto request) {
        Plant plant = new Plant(
                request.getName(),
                request.getCommonInterval(),
                request.getSummerInterval(),
                request.getWinterInterval()
        );

        plantRepository.save(plant);    }
}
