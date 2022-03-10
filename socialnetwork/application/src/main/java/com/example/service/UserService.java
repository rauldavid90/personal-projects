package com.example.service;

import com.example.domain.User;
import com.example.domain.validators.Validator;
import com.example.domain.validators.ValidatorException;
import com.example.repository.Repository;
import com.example.repository.exceptions.RepositoryException;
import com.example.utils.hashAlgorithm.MD5;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserService {
    Repository<Long, User> utilizatori;
    Validator<User> validator;

    /**
     * constructor
     * @param utilizatori
     * @param validator
     */
    public UserService(Repository<Long, User> utilizatori, Validator<User> validator) {
        this.utilizatori = utilizatori;
        this.validator = validator;
    }

    /**
     * creates the user
     * validates the user
     * verifies if there is another identical username
     * adds an user to the list
     * @param firstName
     * @param lastName
     */
    public void add(String firstName, String lastName, String username, String password){
        User user = new User(firstName, lastName, username, password);

        validator.validate(user);

        try{
            getByUsername(username); // verifies if there is another identical username
        }
        catch (NoSuchElementException ex) {
            utilizatori.save(user); // saves the new user if there isn't another identical username
        }
    }

    /**
     * creates the new user
     * validates the new user
     * RepositoryException if there isn't an user with that id
     * updates an user in the list
     * @param id
     * @param firstName new
     * @param lastName new
     */
    public void update(Long id, String firstName, String lastName, String username, String password) {
        User newUser = new User(firstName, lastName, username, password);
        newUser.setId(id);

        validator.validate(newUser);

        if(utilizatori.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        utilizatori.update(newUser);
    }

    /**
     * deletes an user from the list
     * RepositoryException if there isn't an user with that id
     * @param id
     */
    public void delete(Long id){
        if(utilizatori.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        utilizatori.delete(id);
    }

    /**
     * get an user by its id
     * @RepositoryException if there isn't an user with that id
     * @param id
     * @return
     */
    public User get(Long id){
        if(utilizatori.findOne(id) == null)
            throw new RepositoryException("Inexistent id");

        return utilizatori.findOne(id);
    }

    /**
     * get an user by its username
     * throws NoSuchElementException in case there is no user with that username
     * @param username
     * @return
     */
    public User getByUsername(String username){
        List<User> users = StreamSupport.stream(utilizatori.findAll().spliterator(), false)
                .collect(Collectors.toList());
        User searched_user =
                users.stream()
                .filter(x -> {return x.getUsername().equals(username);})
                .findAny()
                .get();
        return searched_user;
    }

    /**
     * get all the users
     * RepositoryException if the list is empty
     * @return
     */
    public Iterable<User> getAll(){
        if(utilizatori.isEmpty())
            throw new RepositoryException("Empty list of users");

        return utilizatori.findAll();
    }

    /**
     * print all the users
     * RepositoryException if the list is empty
     */
    public void showAll(){
        if(utilizatori.isEmpty())
            throw new RepositoryException("Empty list of users");

        for(User u:utilizatori.findAll()) {
            System.out.println(u);
        }
    }

    /**
     * gets the users repo
     * @return
     */
    public Repository<Long, User> getUsersRepo() {
        return utilizatori;
    }

    /**
     * verifies the password for an username
     * throws NoSuchElementException if user not found
     * throws ValidatorException if password is incorrect
     * @param username
     * @param password
     */
    public void verifyPassword(String username, String password){
        User user = getByUsername(username);
        String hashedPass = MD5.getHash(password);
        if(!hashedPass.equals(user.getPassword()))
            throw new ValidatorException("Incorrect password");
    }
}
