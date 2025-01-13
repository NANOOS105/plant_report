package project.plant_report.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.user.request.UserRequestDto;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //유저 등록 서비스
    @Transactional
    public void saveUser(UserRequestDto request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        userRepository.save(user);
    }
}
