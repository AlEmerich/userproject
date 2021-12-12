package fr.technicaltest.userproject.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.technicaltest.userproject.enums.Gender;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
public class CreateUserDto {

    @NotNull
    private String username;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthday;

    private String location;

    private String phone;

    private Gender sexe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getSexe() {
        return sexe;
    }

    public void setSexe(Gender sexe) {
        this.sexe = sexe;
    }
}
