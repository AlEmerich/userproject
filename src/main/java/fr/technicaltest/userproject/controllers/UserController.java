package fr.technicaltest.userproject.controllers;

import fr.technicaltest.userproject.dtos.CreateUserDto;
import fr.technicaltest.userproject.dtos.GetUserDto;
import fr.technicaltest.userproject.exceptions.BusinessException;
import fr.technicaltest.userproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody CreateUserDto userDto) {
        Long id = service.create(userDto);

        try {
            return ResponseEntity.created(new URI("/users/" + id)).build();
        } catch (URISyntaxException e) {
            throw new BusinessException("Error occured during the user creation: id equal to " + id);
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

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> errorHandler(ConstraintViolationException ex) {
        Map<String, String> map = new HashMap<>();
        for(ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            map.put(cv.getPropertyPath().toString(), cv.getMessage());
        }
        return map;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(Exception.class)
    public Map<String, String> errorHandler(Exception ex) {
        Map<String, String> map = new HashMap<>();
        map.put("exception", ex.getClass().getName());
        map.put("message", ex.getMessage());
        return map;
    }
}
