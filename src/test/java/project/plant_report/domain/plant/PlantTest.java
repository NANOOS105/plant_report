package project.plant_report.domain.plant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    public void testGetIntervalForSeason() throws Exception{
        // Given: Plant 객체 생성
        Plant plant = new Plant("Rose", 10, 7, null);

        // When & Then
        assertEquals(7, plant.getIntervalForSeason(Season.SUMMER)); // 여름 물주기
        assertEquals(10, plant.getIntervalForSeason(Season.WINTER)); // 겨울 물주기(공통 사용)
    }


}