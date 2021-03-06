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

        if(userEntity.getCountry() != null &&
                !userEntity.getCountry().equalsIgnoreCase("france")) {
            throw new BusinessException("Le pays doit être égale à France (insensible à la casse)");
        }
    }

    @Transactional
    public Long create(CreateUserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreateDate(new Date());
        userEntity.setBirthday(userDto.getBirthday());
        userEntity.setCountry(userDto.getCountry());
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
