package project.plant_report.controller.plant;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springdoc.core.annotations.ParameterObject;
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
    public ResponseEntity<Void> savePlant(@Valid @RequestBody PlantSaveRequestDto request){
        plantService.savePlant(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<PlantResponseDto> getPlants(
            @RequestParam(required = false) String status,
            @ParameterObject @PageableDefault(size = 20, sort = "nextWateringDate", direction = Sort.Direction.ASC) Pageable pageable){
        return plantService.getPlants(status, pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlant(@PathVariable Long id, @Valid @RequestBody PlantUpdateRequestDto request){
        plantService.updatePlant(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id){
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/water")
    public ResponseEntity<Void> waterPlant (@PathVariable Long id, @RequestParam Season season){
        plantService.waterPlant(id,season);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancelWater")
    public ResponseEntity<Void> cancelWaterPlant(@PathVariable Long id, @RequestParam Season season){
        plantService.cancelWaterPlant(id,season);
        return ResponseEntity.noContent().build();
    }
}
