package project.plant_report.controller.plant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.plant_report.domain.plant.Season;
import project.plant_report.dto.plant.request.PlantSaveRequestDto;
import project.plant_report.dto.plant.request.PlantUpdateRequestDto;
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
    public void savePlant(@RequestBody PlantSaveRequestDto request){
        plantService.savePlant(request);
    }

    @GetMapping
    public List<PlantResponseDto> getPlants(){
        return plantService.getPlants();
    }

    @PutMapping
    public void updatePlant(@RequestBody PlantUpdateRequestDto request){
        plantService.updatePlant(request);
    }

    @DeleteMapping
    public void deletePlant(@RequestParam Long id){
        plantService.deletePlant(id);
    }

    @PostMapping("/water")
    public ResponseEntity<String> waterPlant (@RequestParam Long id, Season season){
        plantService.waterPlant(id,season);
        return ResponseEntity.ok("Watering saved successfully");
    }

    @PutMapping
    public ResponseEntity<String> cancelWaterPlant(@RequestParam Long id, Season season){
        plantService.cancelWaterPlant(id,season);
        return ResponseEntity.ok("CancelWatering saved successfully");
    }
}
