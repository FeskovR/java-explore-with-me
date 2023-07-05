package ru.practicum.service;


import ru.practicum.model.HitDto;
import ru.practicum.model.StatDto;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface StatService {
    void hit(HitDto hit);

    List<StatDto> getStats(String startString,
                                  String endString,
                                  List<String> uris,
                                  boolean unique) throws ValidationException;
}
