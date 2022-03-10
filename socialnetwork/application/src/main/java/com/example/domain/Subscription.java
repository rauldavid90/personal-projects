package com.example.domain;

import java.util.Objects;

public class Subscription extends Entity<Long>{
    private Long user_id;
    private Long event_id;
    private String event_name;
    private String notifications_status;

    /**
     * constructor
     * @param user_id
     * @param event_id
     * @param event_name
     * @param notifications_status
     */
    public Subscription(Long user_id, Long event_id, String event_name, String notifications_status) {
        this.user_id = user_id;
        this.event_id = event_id;
        this.event_name = event_name;
        this.notifications_status = notifications_status;
    }

    /**
     * user id getter
     * @return
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * event id getter
     * @return
     */
    public Long getEvent_id() {
        return event_id;
    }

    /**
     * event name getter
     * @return
     */
    public String getEvent_name(){ return  event_name; }

    /**
     * notification status getter
     * @return
     */
    public String getNotifications_status() {
        return notifications_status;
    }

    /**
     * notification status setter
     * @param notifications_status
     */
    public void setNotifications_status(String notifications_status) {
        this.notifications_status = notifications_status;
    }

    /**
     * string template for printing subscriptions
     * @return
     */
    @Override
    public String toString() {
        return "Subscription{" +
                "user_id=" + user_id +
                ", event_id=" + event_id +
                ", notificationsStatus='" + notifications_status + '\'' +
                '}';
    }

    /**
     * choose how two subscriptions can be equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return getUser_id().equals(that.getUser_id()) &&
                getEvent_id().equals(that.getEvent_id()) &&
                getNotifications_status().equals(that.getNotifications_status());
    }

    /**
     * hash code generator
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUser_id(), getEvent_id(), getNotifications_status());
    }
}
