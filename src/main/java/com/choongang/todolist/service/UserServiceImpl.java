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
    @Transactional
    public User createUser(UserCreateRequestDto userCreateRequestDto) {
        // 이메일 중복 검사
        if (userDao.findByEmail(userCreateRequestDto.getEmail()) != null) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다: " + userCreateRequestDto.getEmail());
        }
        String encoded = passwordEncoder.encode(userCreateRequestDto.getPassword());
        User user = User.builder()
                .email(userCreateRequestDto.getEmail())
                .password(encoded)
                .username(userCreateRequestDto.getName())
                .createAt(LocalDateTime.now())
                .updateAt(null)
                .build();

        int rows = userDao.insertUser(user);
        if (rows != 1) {
            throw new RuntimeException("사용자 생성에 실패했습니다.");
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, String password) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }
        String encoded = passwordEncoder.encode(password);

        if (!passwordEncoder.matches(password, encoded)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
        }

        int rows = userDao.deleteUser(userId);
        if (rows != 1) {
            throw new RuntimeException("사용자 삭제에 실패했습니다.");
        }
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
