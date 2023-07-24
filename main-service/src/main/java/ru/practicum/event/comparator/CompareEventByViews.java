package ru.practicum.event.comparator;

import ru.practicum.event.model.EventShortDto;

import java.util.Comparator;

public class CompareEventByViews implements Comparator<EventShortDto> {
    @Override
    public int compare(EventShortDto event1, EventShortDto event2) {
        return (int)event1.getViews() - (int)event2.getViews();
    }
}
