package project.plant_report.service.plant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true) // 플러시, 더티체킹 방지 -> 조ㅚ 전용 메서드로 불필요한 업데이트 방지
    public Page<PlantResponseDto> getPlants(String status, Pageable pageable) {
        Page<Plant> page = "wateringRequired".equals(status) //NPE 방지
                ? plantRepository.findByNextWateringDateLessThanEqual(LocalDate.now(), pageable)
                : plantRepository.findAll(pageable);
        return page.map(PlantResponseDto::new);
    }

    //식물 수정 서비스
    @Transactional
    public void updatePlant(Long id, PlantUpdateRequestDto request) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
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
    public void cancelWaterPlant(Long plantId, Season season) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new PlantNotFoundException(plantId));
        plant.cancelLastWaterPlant(season);
    }


}
