package project.plant_report.service.plant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.request.PlantCreateRequestDto;
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
    public void savePlant(PlantCreateRequestDto request) {
        Plant plant = new Plant(
                request.getName(),
                request.getCommonInterval(),
                request.getSummerInterval(),
                request.getWinterInterval()
        );

        plantRepository.save(plant);    }

    //식물 조회 서비스
    @Transactional(readOnly = true)
    public List<PlantResponseDto> getPlants(Season currentSeason) {
        return plantRepository.findAll().stream()
                .map(plant -> {
                    int wateringInternal = plant.getIntervalForSeason(currentSeason);
                    String nextWateringDate = calNextWateringDate(wateringInternal);
                    return new PlantResponseDto(plant, wateringInternal, nextWateringDate, currentSeason);
                })
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

    //다음 물주기 날짜 계산

    private String calNextWateringDate(int wateringInterval) {
        LocalDate today = LocalDate.now(); // 오늘 날짜
        LocalDate nextWateringDate = today.plusDays(wateringInterval); // 물주기 간격만큼 더함
        return nextWateringDate.toString(); // 날짜를 문자열로 반환
    }
}
