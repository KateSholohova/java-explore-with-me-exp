package ru.practicum.ViewStats;

import org.mapstruct.Mapper;
import ru.practicum.ViewStatsDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    List<ViewStatsDto> listViewStatsToListViewStatsDto(List<ViewStats> viewStats);
}
