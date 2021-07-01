package eu.deltasource.library.entities;

/**
 * This interface is implement by all observers of {@Link DayLapseEventSource}, and is called when a day have elapsed.
 */
public interface DayLapseObserver {

    void dayLapsed();
}
