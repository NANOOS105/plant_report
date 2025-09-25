package project.plant_report.dto.user.response;

import project.plant_report.dto.plant.response.PlantResponseDto;
import project.plant_report.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserWithPlantsResponseDto {
    private Long id;
    private String email;
    private List<PlantResponseDto> plants;

    public UserWithPlantsResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.plants = user.getPlants().stream()
            .map(PlantResponseDto::new)
            .collect(Collectors.toList());
    }
}
