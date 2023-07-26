package ru.practicum.event.comparator;

import ru.practicum.event.model.Event;

import java.util.Comparator;

public class CompareEventByEventDate implements Comparator<Event> {
    @Override
    public int compare(Event event1, Event event2) {
        if (event1.getEventDate().isBefore(event2.getEventDate())) {
            return -1;
        } else if (event1.getEventDate().isAfter(event2.getEventDate())) {
            return 1;
        } else {
            return 0;
        }
    }
}
