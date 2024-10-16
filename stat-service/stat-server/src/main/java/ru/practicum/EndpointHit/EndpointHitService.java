package ru.practicum.EndpointHit;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.Constants;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStats.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitService {
    private static final Logger log = LoggerFactory.getLogger(EndpointHitService.class);
    private final EndpointHitRepository endpointHitRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void save(EndpointHitDto endpointHit) {
        endpointHitRepository.save(endpointHitMapper.endpointHitDtoToEndpointHit(endpointHit));
    }

    @Transactional(readOnly = true)
    public List<ViewStats> findByParams(String start, String end, List<String> uris, boolean unique) {

        if (LocalDateTime.parse(start, formatter).isAfter(LocalDateTime.parse(end, formatter))) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        List<ViewStats> listViewStats;

        if (CollectionUtils.isEmpty(uris)) {
            uris = endpointHitRepository.findUniqueUri();
        }

        if (unique) {
            listViewStats = endpointHitRepository.findViewStatsByStartAndEndAndUriAndUniqueIp(decodeTime(start),
                    decodeTime(end),
                    uris);
        } else {
            listViewStats = endpointHitRepository.findViewStatsByStartAndEndAndUri(decodeTime(start),
                    decodeTime(end),
                    uris);
        }
        return listViewStats;
    }

    private LocalDateTime decodeTime(String time) {
        String decodeTime = URLDecoder.decode(time, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodeTime, Constants.FORMATTER);
    }
}
