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
                request.getSeason(),
                request.getUser(),
                request.getNotes()
        );
        plantRepository.save(plant);
    }

    //식물 조회 서비스
    @Transactional(readOnly = true)
    public Page<PlantResponseDto> getPlants(String status, Pageable pageable) {
        // status 파라미터는 프론트엔드에서 처리하므로 모든 식물 반환
        Page<Plant> page = plantRepository.findAll(pageable);
        return page.map(PlantResponseDto::new);
    }

    // 테스트 호환용: 인자 없는 getPlants 오버로드
    @Transactional(readOnly = true)
    public List<PlantResponseDto> getPlants() {
        return getPlants(null, Pageable.unpaged()).getContent();
    }

    //식물 수정 서비스
    @Transactional
    public void updatePlant(Long id, PlantUpdateRequestDto request) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
        plant.updatePlant(request.getName(), request.getCommonInterval(), request.getSummerInterval(), request.getWinterInterval(), request.getNotes());
        
        // 마지막 물주기 날짜 업데이트
        if (request.getLastWateringDate() != null) {
            plant.setLastWateringDate(request.getLastWateringDate());
        }
    }

    //식물 삭제 서비스
    @Transactional
    public void deletePlant(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
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