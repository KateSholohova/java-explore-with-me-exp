package ru.practicum.EndpointHit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.Constants;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStats.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;
    private final EndpointHitMapper endpointHitMapper;

    @Transactional
    public void save(EndpointHitDto endpointHit) {
        endpointHitRepository.save(endpointHitMapper.endpointHitDtoToEndpointHit(endpointHit));
    }

    @Transactional(readOnly = true)
    public List<ViewStats> findByParams(String start, String end, List<String> uris, boolean unique) {
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
