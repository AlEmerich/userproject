package fr.technicaltest.userproject.services;

import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.entities.UserEntity;
import fr.technicaltest.userproject.enums.Gender;
import fr.technicaltest.userproject.exceptions.BusinessException;
import fr.technicaltest.userproject.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final UserEntity mockUser = UserEntity.builder()
            .id(1L)
            .username("test")
            .birthday(Date.from(
                    LocalDate.of(1997, 1, 15)
                    .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
            .sexe(Gender.FEMALE)
            .location("Bordeaux")
            .build();

    @Test
    void create_shouldSucceed() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        Long id = userService.create(CreateUserDto.builder()
                        .username(mockUser.getUsername())
                        .sexe(mockUser.getSexe())
                        .birthday(mockUser.getBirthday())
                        .location(mockUser.getLocation())
                .build());

        assertThat(id).isEqualTo(mockUser.getId());
    }

    @Test
    void create_shouldFailedOnlyAdultCanCreateAccount() {
        assertThrows(BusinessException.class, () -> userService.create(CreateUserDto.builder()
                .username(mockUser.getUsername())
                .sexe(mockUser.getSexe())
                .birthday(new Date())
                .location(mockUser.getLocation())
                .build()));
    }

    @Test
    void get_shouldSucceed() {
        when(userRepository.existsById(any(Long.class))).thenReturn(true);

        when(userRepository.getById(any(Long.class))).thenReturn(mockUser);

        GetUserDto returnedUser = userService.get(mockUser.getId());

        assertThat(returnedUser).isNotNull();

        assertThat(returnedUser.getId()).isEqualTo(mockUser.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(returnedUser.getBirthday()).isEqualTo(mockUser.getBirthday());
        assertThat(returnedUser.getLocation()).isEqualTo(mockUser.getLocation());
        assertThat(returnedUser.getSexe()).isEqualTo(mockUser.getSexe().name());
    }

    @Test
    void get_shouldFailed() {
        when(userRepository.existsById(any(Long.class))).thenReturn(false);

        GetUserDto returnedUser = userService.get(mockUser.getId());

        assertThat(returnedUser).isNull();
    }
}