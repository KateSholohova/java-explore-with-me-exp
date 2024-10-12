package ru.practicum.events;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.requests.RequestDto;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
