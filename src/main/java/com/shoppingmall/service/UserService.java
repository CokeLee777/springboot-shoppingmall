package com.shoppingmall.service;

import com.shoppingmall.domain.User;
import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.exception.IncorrectLoginInfoException;
import com.shoppingmall.exception.LoginRequiredException;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shoppingmall.dto.UserRequestDto.*;
import static com.shoppingmall.dto.UserResponseDto.UserProfileInfo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //일반 유저 회원가입
    @Transactional
    public void userRegistration(UserCreateForm form){
        validateDuplicateUser(form);  //중복 회원 검증
        userRepository.save(form.toEntity());
    }
    //중복회원 검증 메서드
    public void validateDuplicateUser(UserCreateForm form) {
        boolean duplicated = userRepository.existsByIdentifier(form.getIdentifier());
        if (duplicated) throw new DuplicatedUserException("이미 등록된 아이디입니다.");
    }

    //유저 로그인
    public UserRole login(LoginUserForm form){
        User findUser = userRepository.findByIdentifier(form.getIdentifier())
                .filter(u -> u.getPassword().equals(form.getPassword()))
                .orElseThrow(() -> new IncorrectLoginInfoException("아이디 또는 비밀번호가 맞지 않습니다."));
        return findUser.getRole();
    }

    //유저 프로필 조회 - 회원 아이디로 조회
    public UserProfileInfo searchProfiles(String identifier){
        User findUser = userRepository.findByIdentifier(identifier).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        return findUser.toUserProfileInfo();
    }

    //유저 프로필 조회 - PK로 조회
    public UserUpdateForm searchProfiles(Long userId){
        User findUser = getUserById(userId);

        return findUser.toUpdateUserForm();
    }

    //유저 프로필 수정
    @Transactional
    public void updateProfiles(Long userId, UserUpdateForm userUpdateForm){
        User findUser = getUserById(userId);
        findUser.updateProfiles(userUpdateForm);
    }

    //유저 탈퇴
    @Transactional
    public void deleteUser(Long userId){
        User findUser = getUserById(userId);
        userRepository.delete(findUser);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));
    }
}
