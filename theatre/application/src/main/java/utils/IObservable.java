package utils;

import java.util.ArrayList;
import java.util.List;

public interface IObservable {
    List<IObserver> observers = new ArrayList<IObserver>();

    void addObserver(IObserver e);
    void removeObserver(IObserver e);
    void notifyObservers();
}
