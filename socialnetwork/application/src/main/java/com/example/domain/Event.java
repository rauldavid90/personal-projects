package com.example.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Event extends Entity<Long>{
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime time;

    /**
     * constructor
     * @param name
     * @param description
     * @param date
     * @param time
     */
    public Event(String name, String description, LocalDate date, LocalTime time) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    /**
     * event name getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * event description getter
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * event date getter
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * event time getter
     * @return
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * string template for printing events
     * @return
     */
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

    /**
     * choose how two events can be equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event that = (Event) o;
        return getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription()) &&
                getDate().equals(that.getDate()) &&
                getTime().equals(that.getTime());
    }

    /**
     * hash code generator
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getDate(), getTime());
    }
}
