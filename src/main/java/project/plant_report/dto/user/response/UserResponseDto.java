package project.plant_report.dto.user.response;

import project.plant_report.dto.plant.response.PlantResponseDto;

import java.util.List;

public class UserResponseDto {
    private Long id;
    private String email;
    private List<PlantResponseDto> plants;
}
