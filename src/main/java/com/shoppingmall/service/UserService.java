package com.shoppingmall.service;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.exception.IncorrectLoginInfoException;
import com.shoppingmall.exception.LoginRequiredException;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //일반 유저 회원가입
    @Transactional
    public void userRegistration(UserRequestDto userRequestDto){
        if(duplicateIdentifierCheck(userRequestDto)){
            userRepository.save(userRequestDto.toEntity());
        }
    }

    //유저 로그인
    public void login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByIdentifier(loginRequestDto.getIdentifier())
                .filter(u -> u.getPassword().equals(loginRequestDto.getPassword()))
                .orElseThrow(() -> new IncorrectLoginInfoException("아이디 또는 삐밀번호가 맞지 않습니다."));
    }

    public boolean duplicateIdentifierCheck(UserRequestDto userRequestDto) {
        if (userRepository.existsByIdentifier(userRequestDto.getIdentifier())) {
            log.error("error={}", "중복 아이디 오류");
            throw new DuplicatedUserException("이미 등록된 아이디입니다.");
        }

        return true;
    }

    public UserResponseDto searchProfiles(String identifier){
        User findUser = userRepository.findByIdentifier(identifier).orElseThrow(() -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        return findUser.toUserResponseDto(findUser);
    }

    public UserResponseDto searchProfiles(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        return findUser.toUserResponseDto(findUser);
    }

    //유저 프로필 수정
    @Transactional
    public UserResponseDto updateProfiles(Long userId, UserRequestDto userRequestDto){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        findUser.updateProfiles(userRequestDto);

        return findUser.toUserResponseDto(findUser);
    }

    //유저 탈퇴
    @Transactional
    public void deleteUser(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(() -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        userRepository.delete(findUser);
    }

}
