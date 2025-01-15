package project.plant_report.service.plant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.request.PlantSaveRequestDto;
import project.plant_report.dto.plant.response.PlantResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlantServiceTest {

    //Mock
    //실제 구현체가 아니라 행동만 설정할 수 있는 가짜 객체 생성
    @Mock
    private PlantRepository plantRepository;

    //InjectMocks
    //테스트 대상 클래스의 인스턴스를 생성하고, 이 클래스가 필요로 하는 의존성을 자동으로 주입
    @InjectMocks
    private PlantService plantService;

    //Mock 객체와 InjectMocks 객체를 초기화 > 테스트가 독립적으로 실행되도록 보장
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("식물 등록 TEST")
    public void savePlant() throws Exception{
        //given
        PlantSaveRequestDto request = new PlantSaveRequestDto(
                "Rose",7,3,null, LocalDate.of(2025,1,1),null
        );
        Plant plant = new Plant(
                request.getName(),
                request.getCommonInterval(),
                request.getSummerInterval(),
                request.getWinterInterval(),
                request.getLastWateringDate(),
                request.getUser()
        );
        when(plantRepository.save(any(Plant.class))).thenReturn(plant);

        //when
        plantService.savePlant(request);

        //then
        verify(plantRepository, times(1)).save(any(Plant.class));
     }

     @Test
     @DisplayName("식물 전체 조회 TEST")
     public void getPlants() throws Exception{
         //given
         Plant plant1 = new Plant(
                 "Rose",7,3,null, LocalDate.of(2025,1,1),null
         );
         Plant plant2 = new Plant(
                 "Tree",10,3,null, LocalDate.of(2024,1,1),null
         );
         when(plantRepository.findAll()).thenReturn(List.of(plant1,plant2));

         //when
         var plants = plantService.getPlants();

         //then
         assertEquals(2,plants.size());
         assertEquals("Rose",plants.get(0).getName());
         assertEquals("Tree",plants.get(1).getName());
         verify(plantRepository,times(1)).findAll();
      }

      @Test
      @DisplayName("식물 물주기 버튼 TEST")
      public void waterPlant() throws Exception{
          //given
          Long plantId = 1L;
          Season season = Season.COMMON;
          Plant plant = new Plant(
                  "Rose",7,3,null, LocalDate.of(2025,1,1),null
          );
          when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

          //when
          plantService.waterPlant(plantId,season);

          //then
          assertTrue(plant.getIsWateringRequired());
          verify(plantRepository,times(1)).findById(plantId);
       }

    @Test
    @DisplayName("식물 물주기 취소 TEST")
    public void cancelWaterPlant() throws Exception{
        //given
        Long plantId = 1L;
        Season season = Season.COMMON;
        Plant plant = new Plant(
                "Rose",7,3,null, LocalDate.of(2025,1,1),null
        );
        plant.waterPlant(season);
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

        //when
        plantService.cancelWaterPlant(plantId,season);

        //then
        assertFalse(plant.getIsWateringRequired());
        verify(plantRepository,times(1)).findById(plantId);
    }


}