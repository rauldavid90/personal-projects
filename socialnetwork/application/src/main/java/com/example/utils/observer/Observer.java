package com.example.utils.observer;

import com.example.utils.events.ObserverEvent;

public interface Observer {
    void update(ObserverEvent observerEvent);
}
