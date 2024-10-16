package ru.practicum.requests;

import java.time.format.DateTimeFormatter;

public class RequestMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static RequestDto toRequestDto(Request request) {
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setEvent(request.getEvent().getId());
        dto.setStatus(request.getStatus());
        dto.setCreated(request.getCreated().format(formatter));
        return dto;
    }
}
