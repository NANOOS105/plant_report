package project.plant_report.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                passwordEncoder.encode(request.getPassword())  // 비밀번호 암호화
        );
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto request){
        User user = userRepository.findById(request.getId())
                .orElseThrow(()->new UserNotFoundException(request.getId()));
        
        // 비밀번호가 제공된 경우에만 암호화
        String encodedPassword = request.getPassword() != null ? 
            passwordEncoder.encode(request.getPassword()) : null;
        user.updateUser(request.getName(), encodedPassword, request.getEmail());
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                        .orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
