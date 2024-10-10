package ru.practicum.requests;

import lombok.Data;

@Data
public class RequestDto {
    private int id;
    private String created;
    private Status status;
    private int requester;
    private int event;
}
