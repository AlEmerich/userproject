package fr.technicaltest.userproject.repositories;

import fr.technicaltest.userproject.entities.UserEntity;
import fr.technicaltest.userproject.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final UserEntity mockUser = UserEntity.builder()
            .username("test")
            .birthday(Date.from(
                    LocalDate.of(1997, 1, 15)
                            .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
            .sexe(Gender.FEMALE)
            .phone("06 06 60 60 60")
            .location("Bordeaux")
            .build();

    @Test
    void saveUser_ShouldSucceed() {
        UserEntity returnedUser = entityManager.persistAndFlush(mockUser);

        assertThat(returnedUser).isNotNull();

        assertThat(returnedUser.getId()).isEqualTo(mockUser.getId());
    }

    @Test
    void saveUser_ShouldFailedEmptyData()  {
        assertThrows(ConstraintViolationException.class,
                () -> entityManager.persistAndFlush(UserEntity.builder().build()));
    }

    @Test
    void saveUser_ShouldFailedPhoneNotValid() {
        mockUser.setPhone("123456789");
        assertThrows(ConstraintViolationException.class,
                () -> entityManager.persistAndFlush(mockUser));
    }

    @Test
    void saveUser_ShouldFailedUsernameEmpty() {
        mockUser.setUsername("");
        assertThrows(ConstraintViolationException.class,
                () -> entityManager.persistAndFlush(mockUser));
    }

    @Test
    void saveUser_ShouldFailedBirthdayNull() {
        mockUser.setBirthday(null);
        assertThrows(ConstraintViolationException.class,
                () -> entityManager.persistAndFlush(mockUser));
    }
}