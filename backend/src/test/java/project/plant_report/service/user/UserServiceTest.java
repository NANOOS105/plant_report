package project.plant_report.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("사용자 등록 테스트")
    public void saveUserTest() throws Exception{
        //given
        UserSaveRequestDto request = new UserSaveRequestDto("ALY","ALY@naver.com","1234");
        String encodedPassword = "encoded1234";
        
        // Mock 설정
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByName(request.getName())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(any(User.class));

        //when
        userService.saveUser(request);

        //then
        // 중복 검사가 호출되었는지 확인
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).existsByName(request.getName());
        
        // 비밀번호 암호화가 호출되었는지 확인
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        
        // 저장이 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
     }
     
     @Test
     @DisplayName("사용자 정보 수정 테스트")
     public void updateUser() throws Exception{
         //given
         Long userId = 1L;
         UserUpdateRequestDto updateRequest = new UserUpdateRequestDto(userId, "AAA", "111", "AAA@naver.com");
         String encodedPassword = "encoded111";
         
         User existingUser = new User("ALY", "ALY@naver.com", "encoded1234");
         when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
         when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn(encodedPassword);

         //when
         userService.updateUser(updateRequest);

         //then
         // 사용자 조회가 호출되었는지 확인
         verify(userRepository, times(1)).findById(userId);
         
         // 비밀번호 암호화가 호출되었는지 확인
         verify(passwordEncoder, times(1)).encode(updateRequest.getPassword());
         
         // 사용자 정보가 업데이트되었는지 확인
         assertThat(existingUser.getName()).isEqualTo("AAA");
         assertThat(existingUser.getEmail()).isEqualTo("AAA@naver.com");
         assertThat(existingUser.getPassword()).isEqualTo(encodedPassword);
      }
      
      @Test
      @DisplayName("중복 이메일로 인한 회원가입 실패 테스트")
      public void saveUserWithDuplicateEmail() throws Exception{
          //given
          UserSaveRequestDto request = new UserSaveRequestDto("ALY","ALY@naver.com","1234");
          when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

          //when & then
          IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
              userService.saveUser(request);
          });
          
          assertThat(exception.getMessage()).contains("이미 사용 중인 이메일입니다");
          verify(userRepository, times(1)).existsByEmail(request.getEmail());
          verify(userRepository, never()).save(any(User.class));
      }
      
      @Test
      @DisplayName("중복 이름으로 인한 회원가입 실패 테스트")
      public void saveUserWithDuplicateName() throws Exception{
          //given
          UserSaveRequestDto request = new UserSaveRequestDto("ALY","ALY@naver.com","1234");
          when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
          when(userRepository.existsByName(request.getName())).thenReturn(true);

          //when & then
          IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
              userService.saveUser(request);
          });
          
          assertThat(exception.getMessage()).contains("이미 사용 중인 닉네임입니다");
          verify(userRepository, times(1)).existsByEmail(request.getEmail());
          verify(userRepository, times(1)).existsByName(request.getName());
          verify(userRepository, never()).save(any(User.class));
      }
      
      @Test
      @DisplayName("사용자 삭제 테스트")
      public void deleteUser() throws Exception{
          //given
          Long userId = 1L;
          User user = new User("ALY", "ALY@naver.com", "encoded1234");
          when(userRepository.findById(userId)).thenReturn(Optional.of(user));

          //when
          userService.deleteUser(userId);

          //then
          verify(userRepository, times(1)).findById(userId);
          verify(userRepository, times(1)).delete(user);
      }

}