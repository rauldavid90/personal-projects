package com.example.service;

import com.example.domain.Subscription;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;
import com.example.repository.exceptions.RepositoryException;
import com.example.utils.events.EventType;
import com.example.utils.events.ObserverEvent;
import com.example.utils.observer.Observable;
import com.example.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionService implements Observable {
    Repository<Long, Subscription> subscriptions;
    Validator<Subscription> validator;
    List<Observer> observableList = new ArrayList<>();

    /**
     * constructor
     * @param subscriptions
     * @param validator
     */
    public SubscriptionService(Repository<Long, Subscription> subscriptions, Validator<Subscription> validator) {
        this.subscriptions = subscriptions;
        this.validator = validator;
    }

    /**
     * adds a subscription
     * @param user_id
     * @param event_id
     * @param notificationsStatus
     */
    public void add(Long user_id, Long event_id, String event_name, String notificationsStatus){
        Subscription subscription = new Subscription(user_id, event_id, event_name, notificationsStatus);

        validator.validate(subscription);

        for(Subscription s : subscriptions.findAll())
            if(subscription.equals(s))
                throw new RepositoryException("Existing subscription");

        subscriptions.save(subscription);

        notifyObservers(new ObserverEvent(EventType.SUBSCRIPTION_ADDED));
    }

    /**
     * deletes a friendship request with the given id
     * RepositoryException if there isn't a friendship request with that id
     * @param id
     */
    public void delete(Long id){
        subscriptions.delete(id);
        notifyObservers(new ObserverEvent(EventType.SUBSCRIPTION_DELETED));
    }

    /**
     * turns on notifications
     * @param id
     */
    public void notificationsSwitch(Long id){
        Subscription subscription = subscriptions.findOne(id);
        if(subscription.getNotifications_status().equals("on"))
            subscription.setNotifications_status("off");
        else
            subscription.setNotifications_status("on");
        subscriptions.update(subscription);

        notifyObservers(new ObserverEvent(EventType.SUBSCRIPTION_NOTIFICATIONS_CHANGED));
    }

    /**
     * get all the subscriptions
     * RepositoryException if the list is empty
     * @return
     */
    public Iterable<Subscription> getAll(){
        if(subscriptions.isEmpty())
            throw new RepositoryException("Empty list of subscriptions");

        return subscriptions.findAll();
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
}
