package fr.technicaltest.userproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.enums.Gender;
import fr.technicaltest.userproject.exceptions.BusinessException;
import fr.technicaltest.userproject.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    void create_shouldSucceed() throws Exception {
        Long id = 1L;

        Date dob =  Date.from(LocalDate.of(1992, 9, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        CreateUserDto userDto = CreateUserDto.builder()
                .birthday(dob)
                .location("mérignac")
                .sexe(Gender.MALE)
                .username("alan")
                .build();

        when(service.create(any(CreateUserDto.class))).thenReturn(id);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                        .andExpect(redirectedUrlPattern("/users/" + id + "*"));
    }

    @Test
    void create_shouldFailedBadPayload() throws Exception {
        CreateUserDto userDto = CreateUserDto.builder()
                .build();

        when(service.create(any(CreateUserDto.class))).thenThrow(new BusinessException("Exception in service"));

        mockMvc.perform(put("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError());
    }

    @Test
    void get_shouldSucceed() throws Exception{
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
    void get_shouldFailed() throws Exception{
        long id = 1L;

        when(service.get(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound());
    }
}