package ru.practicum.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private int id;
    private LocalDateTime created;
    private Status status;
    private int requester;
    private int event;
}
