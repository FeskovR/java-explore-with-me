package ru.practicum.event.comparator;

import ru.practicum.event.model.EventEntity;

import java.util.Comparator;

public class CompareEventByEventDate implements Comparator<EventEntity> {
    @Override
    public int compare(EventEntity event1, EventEntity event2) {
        if (event1.getEventDate().isBefore(event2.getEventDate())) {
            return -1;
        } else if (event1.getEventDate().isAfter(event2.getEventDate())) {
            return 1;
        } else {
            return 0;
        }
    }
}
