package project.plant_report.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.user.request.UserSaveRequestDto;
import project.plant_report.dto.user.request.request.UserUpdateRequestDto;
import project.plant_report.exception.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //유저 등록 서비스
    @Transactional
    public void saveUser(UserSaveRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        if (userRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다: " + request.getName());
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto request){
        User user = userRepository.findById(request.getId())
                .orElseThrow(()->new UserNotFoundException(request.getId()));
        user.updateUser(request.getName(),request.getPassword());
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                        .orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
