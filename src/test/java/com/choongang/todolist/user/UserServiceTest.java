package com.choongang.todolist.user;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.service.UserService;
import com.choongang.todolist.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 테스트는 어떻게 할까?에 대한 예시
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    
    

	private UserService userService;

    // 인터페이스 구현체를 임의적으로 존재한다고 가정했어요
    @Mock
    private UserDao userDao;
    private static final PasswordEncoder PasswordEncoder = null;    // UserService에서 매게변수 추가로 인한 수정 JHE
    
    // 각 테스트가 시작하기 전에 무슨 작업을 실행할지 정하는 메서드에요.
    // 이와 반대로 @BeforeAll은 모든 테스트가 시작하기 전에 딱 한 번만 실행돼요.
    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userDao, PasswordEncoder); // UserService에서 매게변수 추가로 인한 수정 JHE
    }
    
    @Test
    @DisplayName("유저를 생성하는 테스트")
    public void createUserTest() {
        // given (무언가 주어진다면?)
        UserCreateRequestDto createRequestDto = new UserCreateRequestDto();
        createRequestDto.setEmail("test@example.com");
        createRequestDto.setName("tester");
        createRequestDto.setPassword("testpwd");
        createRequestDto.setConfirmPassword("testpwd");
        // Mockito 클래스에서 언제? userDao 클래스의 findByEmail에서 실제 이메일 값을 넣으면! -> 실제로는 null 값이 나온다고
        // "가정"할게요. '실제 환경에서 중복된 이메일이 존재하지 않는다고 가정할게요.'
        Mockito.when(userDao.findByEmail(createRequestDto.getEmail())).thenReturn(null);
        // userDao에서 유저를 저장한다면 리턴 값은 1이라고 나온다고 가정할게요.
//        Mockito.when(userDao.saveUser(Mockito.any())).thenReturn(1);
        // when (어떤 동작을 테스트 할건지?)
        // 유저를 생성해서 리턴되는 유저의 값은 무엇일까요?
        User user = userService.createUser(createRequestDto);
        // then (어떤 결과가 나올지?)
        // Mockito로 확인할게요. userDao를, 1번 발생했다고, 무엇이? saveUser() 메서드가, 지금은 아무값이 들어간다고 가정할게요.
        // 왜 아무값이 들어간다고 가정했을까요? 실제 우리는 유저서비스 내부의 생성된 유저 객체에 접근이 불가능 하기 때문이에요.
        Mockito.verify(userDao, Mockito.times(1)).insertUser(Mockito.any());
        // 생성된 유저 객체의 값이 내가 선언한 값과 같나요 ?
        Assertions.assertEquals(user.getUsername(), createRequestDto.getName());
        Assertions.assertEquals(user.getEmail(), createRequestDto.getEmail());
        Assertions.assertEquals(user.getPassword(), createRequestDto.getPassword());
    }
    
}
