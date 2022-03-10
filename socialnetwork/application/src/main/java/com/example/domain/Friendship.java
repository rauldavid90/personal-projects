package com.example.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    private Long id_friend_1;
    private Long id_friend_2;
    private LocalDate date;
    private LocalTime time;

    /**
     * constructor
     * @param id_friend_1
     * @param id_friend_2
     * @param localDateTime
     */
    public Friendship(Long id_friend_1, Long id_friend_2, LocalDateTime localDateTime) {
        this.id_friend_1 = id_friend_1;
        this.id_friend_2 = id_friend_2;
        this.date=localDateTime.toLocalDate();
        this.time=localDateTime.toLocalTime();
    }

    /**
     * get the id of friend 1 of the friendship
     * @return
     */
    public Long getId_friend_1() {return id_friend_1;}

    /**
     * get the id of friend 2 of the friendship
     * @return
     */
    public Long getId_friend_2() {return id_friend_2;}

    /**
     * set the id of friend 1 of the friendship
     * @param id_friend_1
     */
    public void setId_friend_1(Long id_friend_1) {this.id_friend_1 = id_friend_1;}

    /**
     * set the id of friend 2 of the friendship
     * @param id_friend_2
     */
    public void setId_friend_2(Long id_friend_2) {this.id_friend_2 = id_friend_2;}

    /**
     * string template for printing friendships
     * @return
     */
    @Override
    public String toString() {
        return "Friendship id: " + super.getId() +
                " || Id of friend 1: " + id_friend_1 +
                " || Id of friend 2: " + id_friend_2 +
                " || Date: " + date +
                " || Time: " +time;
    }

    /**
     * choose how two friendships can be equal
     * two friendships are equal if friends have two pairs of identical ids
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return (getId_friend_1().equals(that.getId_friend_1()) &&
                getId_friend_2().equals(that.getId_friend_2())) ||
                (getId_friend_1().equals(that.getId_friend_2()) &&
                getId_friend_2().equals(that.getId_friend_1()));
    }

    /**
     * hash code generator
     * @return
     */
    @Override
    public int hashCode() {return Objects.hash(getId_friend_1(), getId_friend_2());}

    /**
     * return the date when friendship was made
     * @return LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * return the time when friendship was made
     * @return LocatTime
     */
    public LocalTime getTime() {
        return time;
    }
}
