package project.plant_report.controller.user;

import org.springframework.web.bind.annotation.*;
import project.plant_report.dto.user.request.UserSaveRequestDto;
import project.plant_report.dto.user.request.request.UserUpdateRequestDto;
import project.plant_report.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void saveUser(@RequestBody UserSaveRequestDto request){
        userService.saveUser(request);
    }

    @PutMapping
    public void updateUser(@RequestBody UserUpdateRequestDto request){
        userService.updateUser(request);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
    }
}
