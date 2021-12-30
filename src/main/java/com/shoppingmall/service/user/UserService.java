package com.shoppingmall.service.user;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.exception.IncorrectLoginInfoException;
import com.shoppingmall.exception.LoginRequiredException;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //일반 유저 회원가입
    @Transactional
    public Long userRegistration(User user){
        validateDuplicateUser(user);  //중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    //유저 로그인
    public UserRole login(User user){
        User findUser = userRepository.findByIdentifier(user.getIdentifier())
                .filter(u -> u.getPassword().equals(user.getPassword()))
                .orElseThrow(() -> new IncorrectLoginInfoException("아이디 또는 비밀번호가 맞지 않습니다."));
        return findUser.getRole();
    }

    public void validateDuplicateUser(User user) {
        boolean duplicated = userRepository.existsByIdentifier(user.getIdentifier());
        if (duplicated) throw new DuplicatedUserException("이미 등록된 아이디입니다.");
    }

    public UserResponseDto searchProfiles(String identifier){
        User findUser = userRepository.findByIdentifier(identifier).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        return findUser.toUserResponseDto();
    }

    public UserResponseDto searchProfiles(Long userId){
        User findUser = getUserById(userId);

        return findUser.toUserResponseDto();
    }

    //유저 프로필 수정
    @Transactional
    public UserResponseDto updateProfiles(Long userId, UserRequestDto userRequestDto){
        User findUser = getUserById(userId);

        findUser.updateProfiles(userRequestDto);

        return findUser.toUserResponseDto();
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
