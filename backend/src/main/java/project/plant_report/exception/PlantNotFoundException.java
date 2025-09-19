package project.plant_report.exception;

public class PlantNotFoundException extends RuntimeException{
    public PlantNotFoundException(Long id) {
        super("해당 식물이 존재하지 않습니다. ID: " + id);
    }
}
