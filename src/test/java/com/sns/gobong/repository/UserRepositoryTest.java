package com.sns.gobong.repository;

import com.sns.gobong.TestcontainersConfiguration;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.domain.status.VisibilityStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


class UserRepositoryTest extends TestcontainersConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("DB에 유저 save 성공")
    void save() {

        // given
        User user = User.builder()
                .nickname("sonny")
                .tel("010-1111-2222")
                .pw("1234")
                .email("asdfas142@naver.com")
                .visibility(VisibilityStatus.FRIENDS_ONLY)
                .build();

        // when
        User saved = userRepository.save(user);
        Optional<User> find = userRepository.findById(saved.getId());

        // then
        Assertions.assertEquals(find.get().getNickname(), "sonny");
        Assertions.assertEquals(find.get().getNickname(), saved.getNickname());
    }
}