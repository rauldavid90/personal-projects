package com.example.service;

import com.example.domain.Friendship;
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

public class FriendshipService implements Observable {
    Repository<Long, Friendship> friendships;
    Validator<Friendship> validator;
    private List<Observer> observerList = new ArrayList<>();

    /**
     * constructor
     * @param friendships
     * @param validator
     */
    public FriendshipService(Repository<Long, Friendship> friendships, Validator<Friendship> validator) {
        this.friendships = friendships;
        this.validator = validator;
    }

    /**
     * creates the friendship
     * validates the friendship
     * adds a friendship to the list
     * RepositoryException if there is a friendship equal to the new one
     * @param id1 index of user 1
     * @param id2 index of user 2
     */
    public void add(Long id1, Long id2){
        Friendship friendship = new Friendship(id1, id2, LocalDateTime.now());

        validator.validate(friendship);

        for(Friendship p : friendships.findAll())
            if(friendship.equals(p))
                throw new RepositoryException("Existing friendship");

        friendships.save(friendship);
        notifyObservers(new ObserverEvent(EventType.FRIENDSHIP_ADDED));
    }

    /**
     * creates the new friendship
     * validates the new friendship
     * RepositoryException if there isn't a friendship with that id
     * RepositoryException if there is a friendship equal to the new one
     * @param id
     * @param id1 new
     * @param id2 new
     */
    public void update(Long id, Long id1, Long id2){
        Friendship newFriendship = new Friendship(id1, id2,LocalDateTime.now());
        newFriendship.setId(id);

        validator.validate(newFriendship);

        if(friendships.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        for(Friendship f : friendships.findAll())
            if(newFriendship.equals(f))
                throw new RepositoryException("Existing friendship");

        friendships.update(newFriendship);
    }

    /**
     * deletes a friendship with the given id
     * RepositoryException if there isn't a friendship with that id
     * @param id
     */
    public void delete(Long id){
        if(friendships.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        friendships.delete(id);
        notifyObservers(new ObserverEvent(EventType.REMOVE_FRIEND));
    }

    /**
     * get a friendship by its id
     * RepositoryException if there isn't a friendship with that id
     * @param id
     * @return
     */
    public Friendship get(Long id){
        if(friendships.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        return friendships.findOne(id);
    }

    /**
     * get all the friendships
     * RepositoryException if the list is empty
     * @return
     */
    public Iterable<Friendship> getAll(){
        if(friendships.isEmpty())
            throw new RepositoryException("Empty list of friendships");

        return friendships.findAll();
    }

    /**
     * print all the friendships
     * RepositoryException if the list is empty
     */
    public void showAll(){
        if(friendships.isEmpty())
            throw new RepositoryException("Empty list of friendships");

        for(Friendship p: friendships.findAll()) {
            System.out.println(p);
        }
    }

    /**
     * add observer
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    /**
     * notify observer
     * @param observerEvent
     */
    @Override
    public void notifyObservers(ObserverEvent observerEvent) {
        observerList.forEach(x->x.update(observerEvent));
    }

    /**
     * remove observer
     */
    @Override
    public void removeObservers(){
        observerList.clear();
    }
}