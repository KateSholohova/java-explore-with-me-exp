package ru.practicum.events;

import ru.practicum.categories.CategoryMapper;
import ru.practicum.users.UserMapper;

public class EventMapper {

    public static Event fromNewEventDroToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setEventDate(newEventDto.getEventDate());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setTitle(newEventDto.getTitle());
        event.setPaid(newEventDto.isPaid());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setState(event.getState());
        eventFullDto.setId(event.getId());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
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
