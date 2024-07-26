package com.sns.gobong.repository;

import com.sns.gobong.TestcontainersConfiguration;
import com.sns.gobong.config.ValidationConfig;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.domain.status.VisibilityStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(ValidationConfig.class)
class UserRepositoryTest extends TestcontainersConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Validator validator;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("DB에 유저 save 성공")
    @Transactional
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
        assertEquals(find.get().getNickname(), "sonny");
        assertEquals(find.get().getNickname(), saved.getNickname());
    }

    @Test
    @DisplayName("DB에 유저 save 실패 - not null 필드 미기입")
    void saveFail() {
        // given
        User user = User.builder() // 필수 필드인 닉네임이 입력되지 않음
                .tel("010-1111-2222")
                .pw("1234")
                .email("asdfas142@naver.com")
                .visibility(VisibilityStatus.FRIENDS_ONLY)
                .build();

        // when & then
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            userRepository.flush();
        });
    }

    @Test
    @DisplayName("DB에 유저 save 실패 - 전화번호 기입 시 \"-\" 기호 미기입")
    void saveFailByWrongTel() {
        // given
        User user = User.builder()
                .nickname("sonny")
                .tel("01011112222") // 하이픈 미기입
                .pw("1234")
                .email("asdfas142@naver.com")
                .visibility(VisibilityStatus.FRIENDS_ONLY)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("전화번호 형식은 XXX-XXXX-XXXX이어야 합니다."));
    }

    @Test
    @DisplayName("DB에 유저 save 실패 - 틀린 이메일 형식")
    void saveFailByWrongEmail() {
        // given
        User user = User.builder()
                .nickname("sonny")
                .tel("010-1111-2222")
                .pw("1234")
                .email("asdfas1422") // 이메일 형식이 아닌 일반 아이디 형식으로 입력
                .visibility(VisibilityStatus.FRIENDS_ONLY)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("이메일 형식을 지켜주세요."));
    }
}