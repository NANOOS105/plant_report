package project.plant_report.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.user.request.UserSaveRequestDto;
import project.plant_report.dto.user.request.request.UserUpdateRequestDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void saveUserTest() throws Exception{
        //given
        UserSaveRequestDto request = new UserSaveRequestDto("ALY","ALY@naver.com","1234");
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByName(request.getName())).thenReturn(false);
        User savedUser = new User(request.getName(), request.getEmail(), request.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when
        userService.saveUser(request);

        //then
        //중복이 없는지 검증
        assertFalse(userRepository.existsByEmail(request.getEmail()));
        assertFalse(userRepository.existsByName(request.getName()));

        // 저장된 유저 확인
        verify(userRepository, times(1)).save(any(User.class));
     }
     
     @Test
     @DisplayName("식물 업데이트 여부 조회")
     public void updateUser() throws Exception{
         //given
         UserSaveRequestDto saveRequest  = new UserSaveRequestDto("ALY","ALY@naver.com","1234");
         User savedUser = new User(saveRequest.getName(),saveRequest.getEmail(),saveRequest.getPassword());
         when(userRepository.save(any(User.class))).thenReturn(savedUser);
         userService.saveUser(saveRequest);

         UserUpdateRequestDto updateRequest = new UserUpdateRequestDto(1L, "AAA", "111", "AAA@naver.com");
         new User(updateRequest.getName(),updateRequest.getPassword());
         when(userRepository.findById(updateRequest.getId())).thenReturn(Optional.of(savedUser));

         //when
         userService.updateUser(updateRequest);

         //then
         assertThat(savedUser.getName()).isEqualTo("AAA");
         assertThat(savedUser.getPassword()).isEqualTo("111");
         verify(userRepository, times(1)).save(any(User.class));
      }
         
        

}