package fr.technicaltest.userproject.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@Setter
public class ErrorResponse {

    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private String message;

    private Map<String, String> extra;
}
