package project.plant_report.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("해당 유저가 존재하지 않습니다. ID: " + id);
    }
}
