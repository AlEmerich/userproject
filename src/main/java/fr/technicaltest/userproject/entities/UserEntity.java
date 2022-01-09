package fr.technicaltest.userproject.entities;

import fr.technicaltest.userproject.enums.Gender;
import fr.technicaltest.userproject.validators.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue
    private Long id;

    private Date createDate;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotNull
    private Date birthday;

    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String location;

    @Column
    @PhoneNumber
    private String phone;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender sexe = Gender.UNDEFINED;

    public Long getId() {
        return id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

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