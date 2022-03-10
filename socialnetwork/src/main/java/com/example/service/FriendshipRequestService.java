package com.example.service;

import com.example.domain.FriendshipRequest;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;
import com.example.repository.exceptions.RepositoryException;
import com.example.utils.events.ObserverEvent;
import com.example.utils.events.EventType;
import com.example.utils.observer.Observable;
import com.example.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestService implements Observable {
    Repository<Long, FriendshipRequest> friendshipRequests;
    Validator<FriendshipRequest> validator;
    List<Observer> observableList = new ArrayList<>();

    /**
     * constructor
     */
    public FriendshipRequestService(Repository<Long, FriendshipRequest> friendshipRequests, Validator<FriendshipRequest> validator) {
        this.friendshipRequests = friendshipRequests;
        this.validator = validator;
    }

    /**
     * creates the friendship request
     * validates the friendship request
     * adds the friendship request to the database
     * RepositoryException if there is an equal friendship request
     * @param from_id
     * @param to_id
     * @param to_username -representing Last name + first name of user
     * @param from_username -representing Last name + first name of user
     * @param status
     */
    public void add(Long from_id, Long to_id,String to_username,String from_username, String status){
        FriendshipRequest friendshipRequest = new FriendshipRequest(from_id, to_id,to_username,from_username, status, LocalDateTime.now());

        validator.validate(friendshipRequest);

        for(FriendshipRequest r : friendshipRequests.findAll())
            if(friendshipRequest.equals(r))
                throw new RepositoryException("Existing friendship request");

        friendshipRequests.save(friendshipRequest);
    }

    /**
     * deletes a friendship request with the given id
     * RepositoryException if there isn't a friendship request with that id
     * @param id
     */
    public void delete(Long id){
        if(friendshipRequests.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        friendshipRequests.delete(id);
        notifyObservers(new ObserverEvent(EventType.REQUEST_DELETED));
    }

    /**
     * changes the status to approved if it is pending
     * updates tha database
     * @param id
     */
    public void approve(Long id){
        FriendshipRequest request = friendshipRequests.findOne(id);
        if(request.getStatus().equals("pending"))
            request.setStatus("approved");
        else
            throw new RepositoryException();
        friendshipRequests.update(request);
        notifyObservers(new ObserverEvent(EventType.REQUEST_ACCEPTED));
    }

    /**
     * changes the status to rejected
     * updates tha database
     * @param id
     */
    public void reject(Long id){
        FriendshipRequest request = friendshipRequests.findOne(id);
        if(request.getStatus().equals("pending"))
            request.setStatus("rejected");
        else
            throw new RepositoryException();
        friendshipRequests.update(request);
        notifyObservers(new ObserverEvent(EventType.REQUEST_REJECTED));
    }

    /**
     * get a friendship request by its id
     * RepositoryException if there isn't a friendship with that id
     * @param id
     * @return
     */
    public FriendshipRequest get(Long id){
        if(friendshipRequests.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        return friendshipRequests.findOne(id);
    }

    /**
     * get all the friendships
     * RepositoryException if the list is empty
     * @return
     */
    public Iterable<FriendshipRequest> getAll(){
        if(friendshipRequests.isEmpty())
            throw new RepositoryException("Empty list of friendship requests");

        return friendshipRequests.findAll();
    }

    /**
     * print all the friendship requests
     * RepositoryException if the list is empty
     */
    public void showAll(){
        if(friendshipRequests.isEmpty())
            throw new RepositoryException("Empty list of friendship requests");

        for(FriendshipRequest r: friendshipRequests.findAll()) {
            System.out.println(r);
        }
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
