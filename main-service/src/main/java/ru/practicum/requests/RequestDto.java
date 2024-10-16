package ru.practicum.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
    private int id;
    private String created;
    private Status status;
    private int requester;
    private int event;
}
