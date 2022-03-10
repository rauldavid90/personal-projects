package com.example.utils.observer;

import com.example.utils.events.ObserverEvent;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers(ObserverEvent observerEvent);
    void removeObservers();
}
