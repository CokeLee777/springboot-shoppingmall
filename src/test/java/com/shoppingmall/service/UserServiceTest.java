package com.shoppingmall.service;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.shoppingmall.dto.UserRequestDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    @Test
    @DisplayName("회원가입 테스트")
    public void join () throws Exception
    {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("test123", "test123*", "test1", "test@naver.com", "test", "test");
        //when
        userService.userRegistration(userRequestDto.toEntity());
        //then
        Optional<User> findUser = userRepository.findByIdentifier("test123");
        assertThat(userRequestDto.getEmail()).isEqualTo(findUser.get().getEmail());
    }

    @Test
    @DisplayName("중복 아이디 회원가입 테스트")
    public void duplicatedJoin () throws Exception
    {
        //given
        UserRequestDto userRequestDto1 = new UserRequestDto("test123", "test123*", "test1", "test@naver.com", "test", "test");
        UserRequestDto userRequestDto2 = new UserRequestDto("test123", "test123*", "test2", "test2@naver.com", "test2", "test2");
        //when
        userService.userRegistration(userRequestDto1.toEntity());
        //then
        assertThrows(DuplicatedUserException.class,
                () -> userService.userRegistration(userRequestDto2.toEntity()));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void login () throws Exception
    {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("test123", "test123*", "test1", "test@naver.com", "test", "test");
        userService.userRegistration(userRequestDto.toEntity());
        //when
        LoginRequestDto loginRequestDto = new LoginRequestDto("test123", "test123*");
        userService.login(loginRequestDto.toEntity());
        //then
    }

    @Test
    @DisplayName("회원 프로필 조회 테스트")
    public void searchProfiles () throws Exception
    {
        //given
        User user = new User();
        userRepository.save(user);

        //when
        UserResponseDto findUserResponseDto = userService.searchProfiles(user.getId());
        //then
        assertThat(findUserResponseDto.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("회원 프로필 수정 테스트")
    public void updateProfiles () throws Exception
    {
        //given
        User user = new User();
        userRepository.save(user);
        UserRequestDto userRequestDto = new UserRequestDto("test123", "test123*", "test1", "test@naver.com", "test", "test");
        //when
        UserResponseDto userResponseDto = userService.updateProfiles(user.getId(), userRequestDto);
        //then
        Optional<User> findUser = userRepository.findById(user.getId());
        assertThat(findUser.get().getIdentifier()).isEqualTo(userResponseDto.getIdentifier());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    public void deleteUser () throws Exception
    {
        //given
        User user = new User();
        userRepository.save(user);
        assertThat(userRepository.findById(user.getId()).orElse(null)).isNotNull();
        //when
        userService.deleteUser(user.getId());
        //then
        assertThat(userRepository.findById(user.getId()).orElse(null)).isNull();
    }
}