package com.shoppingmall.service;

import com.shoppingmall.domain.User;
import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.shoppingmall.dto.UserRequestDto.*;
import static com.shoppingmall.dto.UserResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("회원가입 테스트")
    public void join () throws Exception
    {
        //given
        UserCreateForm userCreateForm = new UserCreateForm("test1234", "test123*", "test1", "test@naver.com", "test", "test");
        //when
        userService.userRegistration(userCreateForm);
        //then
        Optional<User> findUser = userRepository.findByIdentifier("test1234");
        assertThat(userCreateForm.getEmail()).isEqualTo(findUser.get().getEmail());
    }

    @Test
    @DisplayName("중복 아이디 회원가입 테스트")
    public void duplicatedJoin () throws Exception
    {
        //given
        UserCreateForm userCreateForm1 = new UserCreateForm("test1234", "test123*", "test1", "test@naver.com", "test", "test");
        UserCreateForm userCreateForm2 = new UserCreateForm("test1234", "test123*", "test2", "test2@naver.com", "test2", "test2");
        //when
        userService.userRegistration(userCreateForm1);
        //then
        assertThrows(DuplicatedUserException.class,
                () -> userService.userRegistration(userCreateForm2));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void login () throws Exception
    {
        //given
        UserCreateForm userCreateForm = new UserCreateForm("test1234", "test123*", "test1", "test@naver.com", "test", "test");
        userService.userRegistration(userCreateForm);
        //when
        LoginUserForm loginUserForm = new LoginUserForm(UserRole.USER,"test1234", "test123*");
        userService.login(loginUserForm);
        //then
    }

    @Test
    @DisplayName("회원 프로필 조회 테스트")
    public void searchProfiles () throws Exception
    {
        //given
        User user = new User();
        user.setIdentifier("test111");
        userRepository.save(user);

        //when
        UserProfileInfo userProfileInfo = userService.searchProfiles(user.getIdentifier());
        //then
        assertThat(userProfileInfo.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("회원 프로필 수정 테스트")
    public void updateProfiles () throws Exception
    {
        //given
        UserCreateForm userCreateForm = new UserCreateForm("test1234", "test123*", "beforeName", "test@naver.com", "test", "test");
        userService.userRegistration(userCreateForm);
        //when
        UserProfileInfo userProfileInfo = userService.searchProfiles("test1234");
        UserUpdateForm userUpdateForm = new UserUpdateForm("test1234", "test123*", "afterName", "test@naver.com", "test", "test");
        userService.updateProfiles(userProfileInfo.getId(), userUpdateForm);
        //then
        Optional<User> findUser = userRepository.findById(userProfileInfo.getId());
        assertThat(findUser.get().getUsername()).isEqualTo("afterName");
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