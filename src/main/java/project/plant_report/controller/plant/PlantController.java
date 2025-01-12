package project.plant_report.controller.plant;

import org.springframework.web.bind.annotation.*;
import project.plant_report.dto.plant.request.PlantRequestDto;
import project.plant_report.dto.plant.response.PlantResponseDto;
import project.plant_report.service.plant.PlantService;

import java.util.List;

@RestController
@RequestMapping("/api/plant")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService){
        this.plantService = plantService;
    }

    @PostMapping
    public void savePlant(@RequestBody PlantRequestDto request){
        plantService.savePlant(request);
    }

    @GetMapping
    public List<PlantResponseDto> getPlants(){
        return plantService.getPlant();
    }
}
