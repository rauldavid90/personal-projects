package com.example.utils.events;

public class ObserverEvent {
    private EventType type;

    public ObserverEvent(EventType type){
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
