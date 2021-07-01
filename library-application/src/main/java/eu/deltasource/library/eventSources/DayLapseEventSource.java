package eu.deltasource.library.eventSources;

import eu.deltasource.library.entities.DayLapseObserver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Event source for all classes subscribed to the observers list.
 */
public class DayLapseEventSource {

    public static DayLapseEventSource getInstance() {
        return instance;
    }

    private static final DayLapseEventSource instance = new DayLapseEventSource();

    private List<DayLapseObserver> observers = new ArrayList<>();

    public void addObserver(DayLapseObserver observer) {
        this.observers.add(observer);
    }

    public void dayLapsed() {
        for (DayLapseObserver observer : observers) {
            observer.dayLapsed();
        }
    }
}
