package project.plant_report.service.plant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import project.plant_report.domain.plant.Plant;
import project.plant_report.domain.plant.PlantRepository;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.response.PlantResponseDto;

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
    public void getPlantsTest() throws Exception{
        // given
        Plant plant1 = new Plant("Monstera", 10, 7, 15);
        Plant plant2 = new Plant("Ficus", 14, 10, 20);
        when(plantRepository.findAll()).thenReturn(List.of(plant1, plant2));

        // when
        List<PlantResponseDto> result = plantService.getPlants(Season.SUMMER);

        // then
        assertEquals(2, result.size()); // 두 개의 식물이 반환되어야 함
        assertEquals(7, result.get(0).getWateringInterval()); // 첫 번째 식물의 여름 간격 확인
        assertEquals(10, result.get(1).getWateringInterval()); // 두 번째 식물의 여름 간격 확인
     }

     @Test
     public void deletePlant() throws Exception{
         //given
         Long plantId = 1L;
         Plant plant = new Plant("Monstera", 10, 7, 15);
         when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

         //when
         plantService.deletePlant(plantId);

         //then
         verify(plantRepository, times(1)).delete(plant);
      }


}