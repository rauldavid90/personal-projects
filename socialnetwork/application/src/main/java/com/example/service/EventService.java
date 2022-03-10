package com.example.service;

import com.example.domain.Event;
import com.example.domain.FriendshipRequest;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;
import com.example.repository.exceptions.RepositoryException;
import com.example.utils.PageEvent;
import com.example.utils.events.EventType;
import com.example.utils.events.ObserverEvent;
import com.example.utils.observer.Observable;
import com.example.utils.observer.Observer;
import javafx.beans.InvalidationListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventService implements Observable {
    Repository<Long, Event> events;
    Validator<Event> validator;
    List<Observer> observableList = new ArrayList<>();
    private int pageNumber = 0;
    private int pageSize = 8;

    /**
     * constructor
     * @param events
     * @param validator
     */
    public EventService(Repository<Long, Event> events, Validator<Event> validator) {
        this.events = events;
        this.validator = validator;
    }

    /**
     * creates the event
     * validates the event
     * adds the event to the database
     * RepositoryException if there is an equal event
     * notifies observers
     * @param name
     * @param description
     * @param date
     * @param time
     */
    public void add(String name, String description, LocalDate date, LocalTime time){
        Event event = new Event(name, description, date, time);

        validator.validate(event);

        for(Event e : events.findAll())
            if(event.equals(e))
                throw new RepositoryException("Existing event");

        events.save(event);

        notifyObservers(new ObserverEvent(EventType.EVENT_ADDED));
    }

    /**
     * get an event from the database
     * @param id
     * @return
     */
    public Event get(Long id){
        if(events.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        return events.findOne(id);
    }

    /**
     * get all the events
     * RepositoryException if the list is empty
     * @return
     */
    public Iterable<Event> getAll(){
        if(events.isEmpty())
            throw new RepositoryException("Empty list of events");

        return events.findAll();
    }

    /**
     * add observer
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        observableList.add(observer);
    }

    /**
     * notify observer
     * @param observerEvent
     */
    @Override
    public void notifyObservers(ObserverEvent observerEvent) {
        observableList.forEach(x->x.update(observerEvent));
    }

    /**
     * remove observer
     */
    @Override
    public void removeObservers() {
        observableList.clear();
    }

    public List<Event> getPage(PageEvent type) throws Exception {
        if(type==PageEvent.NEXT_PAGE) pageNumber++;
        if(type==PageEvent.PREVIOUS_PAGE && pageNumber > 0) pageNumber--;
        List<Event> lista = StreamSupport.stream(events.findAll().spliterator(),false).collect(Collectors.toList());
        int finalPos = pageNumber * pageSize + pageSize;
        if(finalPos > lista.size()) finalPos = lista.size();
        try {
            lista = lista.subList(pageNumber * pageSize, finalPos);
        }catch (Exception e){
            lista.clear();
        }
        if(lista.isEmpty()){
            pageNumber --;
            throw new Exception("Nu exista alte pagini");
        }
        return lista;
    }
}
