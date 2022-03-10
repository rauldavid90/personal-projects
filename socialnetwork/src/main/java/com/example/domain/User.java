package com.example.domain;

import java.util.*;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String username;
    private String password;


    /**
     * constructor
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     */
    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    /**
     * get the first name of an user
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * set the first name of an user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * get the last name of an user
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * set the last name of an user
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * username getter
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * username setter
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * password getter
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * password setter
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * string template for printing users
     * @return
     */
    @Override
    public String toString() {
        return  "Id: " + super.getId() +
                " || First name: " + firstName +
                " || Last name: " + lastName  +
                " || Username: " + username +
                " || Password: " + password +
                " || ";
    }

    /**
     * choose how two users can be equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getUsername().equals(that.getUsername()) &&
                getPassword().equals(that.getPassword());
    }

    /**
     * hash code generator
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}