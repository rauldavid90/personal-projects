package com.example.domain;

import java.util.List;

public class ChatRoom extends Entity<Long> {
    private List<User> members;
    private String name;

    /**
     * Constructor
     * @param members List<User> representing chatroom members
     * @param name String representing chatroom names
     */
    public ChatRoom(List<User> members, String name) {
        this.members = members;
        this.name = name;
    }

    /**
     * Returns the list of members
     * @return List<User>
     */
    public List<User> getMembers() {
        return members;
    }

    /**
     * Returns chatroom names
     * @return
     */
    public String getName() {
        return name;
    }
}
