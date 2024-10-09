package ru.practicum.events;

import ru.practicum.categories.CategoryMapper;
import ru.practicum.users.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        eventFullDto.setState(event.getState());
        eventFullDto.setId(event.getId());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }


    public static Event fromNewEventDroToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setTitle(newEventDto.getTitle());
        event.setPaid(newEventDto.getPaid());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        return event;
    }


    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setId(event.getId());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setViews(event.getViews());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        return eventShortDto;
    }

}
