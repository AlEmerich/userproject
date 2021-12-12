package fr.technicaltest.userproject.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.technicaltest.userproject.controllers.UserController;
import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCreate() throws Exception {
        Long id = 1L;

        Date dob =  Date.from(LocalDate.of(1992, 9, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        CreateUserDto userDto = CreateUserDto.builder()
                .birthday(dob)
                .location("mérignac")
                .sexe(Gender.MALE)
                .username("alan")
                .build();

        when(service.create(any(CreateUserDto.class))).thenReturn(id);

        mockMvc.perform(put("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                        .andExpect(redirectedUrlPattern("/users/" + id + "*"));
    }

    @Test
    void testGet() throws Exception{
        Long id = 1L;

        Date dob =  Date.from(LocalDate.of(1992, 9, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        GetUserDto userDto = GetUserDto.builder()
                .id(id)
                .birthday(dob)
                .location("mérignac")
                .sexe("MALE")
                .username("alan")
                .build();

        when(service.get(any(Long.class))).thenReturn(userDto);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
    }

    @Test
    void testGetNotOk() throws Exception{
        Long id = 1L;

        Date dob =  Date.from(LocalDate.of(1992, 9, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        GetUserDto userDto = GetUserDto.builder()
                .id(id)
                .birthday(dob)
                .location("mérignac")
                .sexe("MALE")
                .username("alan")
                .build();

        when(service.get(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound());
    }
}