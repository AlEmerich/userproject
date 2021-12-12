package fr.technicaltest.userproject.services;

import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.entities.UserEntity;
import fr.technicaltest.userproject.exceptions.BusinessException;
import fr.technicaltest.userproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    private void validate(UserEntity userEntity) {
        if(userEntity.getBirthday() != null) {
            int age = Period.between(userEntity.getBirthday().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(), LocalDate.now()).getYears();
            if(age < 18) {
                throw new BusinessException("Only adults can create account");
            }
        } else {
            throw new BusinessException("The birth date is mandatory");
        }

        if(userEntity.getPhone() != null) {
            Pattern pattern =
                    Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
            Matcher matcher = pattern.matcher(userEntity.getPhone());
            if(!matcher.matches()) {
                throw new BusinessException("Phone is not in the right format (ex: 06 00 00 00 00)");
            }
        }
    }

    @Transactional
    public Long create(CreateUserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreateDate(new Date());
        userEntity.setBirthday(userDto.getBirthday());
        userEntity.setLocation(userDto.getLocation());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setSexe(userDto.getSexe());
        userEntity.setUsername(userDto.getUsername());

        validate(userEntity);

        return repository.save(userEntity).getId();
    }

    public GetUserDto get(Long id) {
        if(!repository.existsById(id)) {
            return null;
        }

        UserEntity userEntity = repository.getById(id);
        return GetUserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .birthday(userEntity.getBirthday())
                .location(userEntity.getLocation())
                .sexe(userEntity.getSexe().name())
                .phone(userEntity.getPhone())
                .build();
    }
}
