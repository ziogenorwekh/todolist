package com.choongang.todolist.service;

import com.choongang.todolist.dao.UserDao;
import com.choongang.todolist.domain.User;
import com.choongang.todolist.dto.UserCreateRequestDto;
import com.choongang.todolist.dto.UserUpdateDto;
import com.choongang.todolist.exception.DuplicateEmailException;
import com.choongang.todolist.exception.InvalidPasswordException;
import com.choongang.todolist.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor  //final 필드나 @NonNull 필드를 대상으로 생성자를 자동 생성해줌
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
	private PasswordEncoder passwordEncoder;   // 비밀번호 해시 저장 JHE

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;   // JHE
    }
    
 
    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        // 이메일 중복 확인
        User existingUser = userDao.findByEmail(userCreateRequestDto.getEmail());
        if (existingUser != null) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다: " + userCreateRequestDto.getEmail());
        }

        // 주석을 참고해주세요.
        // 유저 객체를 Builder를 사용해서 생성하시거나, 생성자를 이용해서 사용하는 것이 조금 더 바람직 합니다.
//        User user = User.createUser(userCreateRequestDto);
//        유저의 updateAt은 새롭게 생성한 유저라면, 업데이트 기록이 없으니 null 처리 해주시면 좋을 것 같습니다.

        // User 객체 생성
        User user = new User();
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPassword(userCreateRequestDto.getPassword());
        user.setUsername(userCreateRequestDto.getName());
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        // 데이터베이스에 저장
        userDao.insertUser(user);

        return user;
    }

    // userDao를 사용해서 delete user를 실행했어요. 그 다음 행동을 무엇일까요?
    // 개인적으로 성공적으로 처리되었는지 아닌지는 확인해주셔야 합니다.
    // 일련의 에러로 인해 실제 반환값이 0이라면?(변환된 행의 개수가 0 <- 즉 수정(삭제)되지 않음)
    // 사용자는 삭제되었다고 생각하지만, 실제로는 데이터베이스에 삭제되지 않았음을 알 수 있습니다.
    @Override
    public void deleteUser(Long userId, String password) {
        // 사용자 존재 확인
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
        }

        // 사용자 삭제
        userDao.deleteUser(userId);
    }
    
    /**
     * 회원 정보를 수정한다.
     * <p>
     * - 본인만 수정 가능 (actorUserId == targetUserId)
     * - 프로필(이름, 이메일, 프로필 이미지)과 비밀번호를 수정할 수 있다.
     * - 비밀번호가 입력되면 해시화 후 저장된다.
     *
     * @param targetUserId 수정 대상 사용자 ID (DB에서 업데이트할 회원)
     * @param actorUserId  수행자(로그인한 사용자 ID). 본인만 수정 가능.
     * @param Dto         수정할 데이터가 담긴 DTO (이름, 이메일, 프로필 이미지, 새 비밀번호 등)
     * @throws org.springframework.security.access.AccessDeniedException 본인이 아닌 경우
     * @throws IllegalArgumentException 비밀번호 확인(newPasswordConfirm)이 일치하지 않는 경우
     */
    @Transactional
    public void updateUser(Long targetUserId, Long actorUserId, UserUpdateDto Dto) {
        // 권한 체크: 본인만
        if (!targetUserId.equals(actorUserId)) {
            throw new AccessDeniedException("본인만 수정할 수 있습니다.");
        }

        // 1) 프로필 업데이트
        userDao.updateProfile(
                targetUserId,
                Dto.getDisplayName(),
                Dto.getEmail(),
                Dto.getProfileImageUrl()
        );

        // 2) 비밀번호 변경 요청이 있으면 처리
        if (Dto.getNewPassword() != null && !Dto.getNewPassword().isBlank()) {
            if (!Dto.getNewPassword().equals(Dto.getNewPasswordConfirm())) {
                throw new IllegalArgumentException("새 비밀번호와 확인이 일치하지 않습니다.");
            }
            String hash = passwordEncoder.encode(Dto.getNewPassword().trim());
            userDao.updatePassword(targetUserId, hash);
        }
    }
}
