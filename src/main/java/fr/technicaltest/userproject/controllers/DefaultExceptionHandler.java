package fr.technicaltest.userproject.controllers;

import fr.technicaltest.userproject.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse errorHandler(ConstraintViolationException ex) {
        Map<String, String> map = new HashMap<>();
        for(ConstraintViolation<?> cv : ex.getConstraintViolations()) {
            map.put(cv.getPropertyPath().toString(), cv.getMessage());
        }

        return ErrorResponse.builder()
                .timestamp(new Date())
                .message("Field constraints violations.")
                .extra(map)
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ErrorResponse errorHandler(BusinessException ex) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();
    }
}
