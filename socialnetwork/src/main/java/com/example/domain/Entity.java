package com.example.domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     * get id of entity
     * @return
     */
    public ID getId() {
        return id;
    }

    /**
     * set id of entity
     * @param id
     */
    public void setId(ID id) {
        this.id = id;
    }
}