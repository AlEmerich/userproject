package fr.technicaltest.userproject.controllers;

import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.exceptions.BusinessException;
import fr.technicaltest.userproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUserDto userDto) {
        Long id = service.create(userDto);

        try {
            return ResponseEntity.created(new URI("/users/" + id)).build();
        } catch (URISyntaxException e) {
            throw new BusinessException("Error occurred during the user creation: id equal to " + id);
        }
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<GetUserDto> get(@PathVariable Long id) {
        GetUserDto getUserDto = service.get(id);
        if(getUserDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getUserDto);
    }
}
