package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.events.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    public void delete(int compId) {
        if (compilationRepository.existsById(compId)) {
            compilationRepository.deleteById(compId);
        } else {
            throw new NotFoundException("Compilation not found");
        }
    }

    public CompilationDto update(int compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        //Compilation newCompilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    public List<CompilationDto> getAll(boolean pinned, int from, int size) {
        List<Compilation> comps = compilationRepository.findAllByPinned(pinned).subList(from, from + size);
        return comps.stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();
    }

    public CompilationDto findById(int compId) {
        if (compilationRepository.existsById(compId)) {
            return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).get());
        } else {
            throw new NotFoundException("Compilation not found");
        }
    }
}
