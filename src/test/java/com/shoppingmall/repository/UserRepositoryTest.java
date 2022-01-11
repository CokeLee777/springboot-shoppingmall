package com.shoppingmall.repository;

import com.shoppingmall.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml")
@Transactional
class UserRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("회원 저장 테스트")
    public void save () throws Exception
    {
        //given
        User user = new User();
        User savedUser = userRepository.save(user);
        //when
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        //then
        assertThat(savedUser).isInstanceOf(findUser.get().getClass());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    public void update () throws Exception
    {
        //given
        User user = new User();
        User savedUser = userRepository.save(user);
        String prevPwd = savedUser.getPassword();
        em.flush();
        em.clear();
        //when
        savedUser.setPassword("123");
        //then
        assertThat(prevPwd).isNotEqualTo("1234");
    }

    @Test
    @DisplayName("회원 조회 테스트")
    public void search () throws Exception
    {
        //given
        User user = new User();
        User savedUser = userRepository.save(user);
        //when
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        //then
        assertThat(savedUser).isInstanceOf(findUser.get().getClass());
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    public void delete () throws Exception
    {
        //given
        User user = new User();
        User savedUser = userRepository.save(user);
        Long savedId = savedUser.getId();
        //when
        userRepository.delete(savedUser);
        Optional<User> findUser = userRepository.findById(savedId);
        //then
        assertThat(findUser.orElse(null)).isNull();
    }
}