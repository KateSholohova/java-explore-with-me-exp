package ru.practicum.events;

import lombok.Data;
import ru.practicum.requests.RequestDto;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
