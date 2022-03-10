package com.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    User from;
    Long to;
    String message;
    LocalDateTime date;
    Long reply;

    /**
     * constructor
     *
     * @param from
     * @param to
     * @param message
     * @param date
     */
    public Message(User from, Long to, String message, LocalDateTime date,Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply=reply;
    }

    public User getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getReply() {
        return reply;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }
}