package com.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendshipRequest extends Entity<Long>{
    private Long from_id;
    private Long to_id;
    private String status;
    private LocalDateTime date;
    private String to_username;
    private String from_username;

    /**
     * constructor
     * @param from_id
     * @param to_id
     * @param status
     */
    public FriendshipRequest(Long from_id, Long to_id,String to_username,String from_username, String status, LocalDateTime date) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.status = status;
        this.date=date;
        this.to_username = to_username;
        this.from_username = from_username;
    }

    /**
     * get username of user that received the request
     * @return
     */
    public String getTo_username() {
        return to_username;
    }

    /**
     * get username of user that sent the request
     * @return
     */
    public String getFrom_username() {
        return from_username;
    }

    /**
     * get id of user that sent the request
     * @return
     */
    public Long getFrom_id() {
        return from_id;
    }

    /**
     * get id of user that received the request
     * @return
     */
    public Long getTo_id() {
        return to_id;
    }

    /**
     * get the status of the friendship request
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * get the date when the request was created
     * @return
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * sets a new status for the friendship request
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * string template for printing friendship requests
     * @return
     */
    @Override
    public String toString() {
        return "Friendship request id: " + super.getId() +
                " || From id: " + from_id +
                " || To id: " + to_id +
                " || Status: " + status;
    }

    /**
     * choose how two friendship requests can be equal
     * two friendship requests are equal if they contain the same user ids
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendshipRequest)) return false;
        FriendshipRequest that = (FriendshipRequest) o;
        return ((getFrom_id().equals(that.getFrom_id()) &&
                getTo_id().equals(that.getTo_id())) ||
                (getFrom_id().equals(that.getTo_id()) &&
                getTo_id().equals(that.getFrom_id()))) &&
                getStatus().equals(that.getStatus());
    }

    /**
     * hash code generator
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFrom_id(), getTo_id());
    }
}
