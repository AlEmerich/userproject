package fr.technicaltest.userproject.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class ErrorResponse {

    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private String message;

    @Builder.Default
    private Map<String, String> extra = new HashMap<>();
}
