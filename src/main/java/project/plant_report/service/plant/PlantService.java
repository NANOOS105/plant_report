package project.plant_report.service.plant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.request.PlantSaveRequestDto;
import project.plant_report.dto.plant.request.PlantUpdateRequestDto;
import project.plant_report.dto.plant.response.PlantResponseDto;
import project.plant_report.exception.PlantNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    //식물 등록 서비스
    @Transactional
    public void savePlant(PlantSaveRequestDto request) {
        Plant plant = new Plant(
                request.getName(),
                request.getCommonInterval(),
                request.getSummerInterval(),
                request.getWinterInterval(),
                request.getLastWateringDate(),
                request.getUser()
        );

        plantRepository.save(plant);
    }

    //식물 조회 서비스
    @Transactional(readOnly = true)
    public List<PlantResponseDto> getPlants() {
        return plantRepository.findAll().stream()
                .map(PlantResponseDto::new)
                .collect(Collectors.toList());
    }

    //식물 수정 서비스
    @Transactional
    public void updatePlant(PlantUpdateRequestDto request) {
        Plant plant = plantRepository.findById(request.getId())
                .orElseThrow(() -> new PlantNotFoundException(request.getId()));
        plant.updatePlant(request.getName(), request.getCommonInterval(),request.getSummerInterval(), request.getWinterInterval());
    }

    //식물 삭제 서비스
    @Transactional
    public void deletePlant(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(()->new PlantNotFoundException(id));
        plantRepository.delete(plant);

    }

    // 물주기
    @Transactional
    public void waterPlant(Long plantId, Season season) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException(plantId));
        plant.waterPlant(season);
    }

    // 물주기 취소
    @Transactional
    public void cancelWatering(Long plantId, Season season) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException(plantId));
        plant.cancelLastWaterPlant(season);
    }


}
