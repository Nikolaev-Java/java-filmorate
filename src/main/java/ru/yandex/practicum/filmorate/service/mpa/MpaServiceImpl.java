package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;
    @Override
    public List<Mpa> findAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(int id) {
        return mpaRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("No Mpa found with id: " + id,"Mpa"));
    }
}
