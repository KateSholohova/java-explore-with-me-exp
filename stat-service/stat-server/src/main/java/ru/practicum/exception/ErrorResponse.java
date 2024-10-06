package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.Constants;

import java.time.LocalDateTime;


public record ErrorResponse(String status, String reason, String description,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT) LocalDateTime errorTime) {
}
