package project.plant_report.service.plant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.request.PlantRequestDto;
import project.plant_report.dto.plant.response.PlantResponseDto;

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
    public void savePlant(PlantRequestDto request) {
        Plant plant = new Plant(
                request.getName(),
                request.getCommonInterval(),
                request.getSummerInterval(),
                request.getWinterInterval()
        );

        plantRepository.save(plant);    }

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

    //다음 물주기 날짜 계산
    private String calNextWateringDate(int wateringInterval) {
        LocalDate today = LocalDate.now(); // 오늘 날짜
        LocalDate nextWateringDate = today.plusDays(wateringInterval); // 물주기 간격만큼 더함
        return nextWateringDate.toString(); // 날짜를 문자열로 반환
    }
}
