package com.example.service;

import com.example.domain.ChatRoom;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.Repository;
import org.w3c.dom.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService  {
    private Repository<Long, Message> messages;

    public MessageService(Repository<Long, Message> messages) {
        this.messages = messages;
    }

    /**
     * create a new message
     * @param from User
     * @param to List<User>
     * @param text String
     */
    public void newMessage(User from, Long to,String text){
        Message message=new Message(from,to,text, LocalDateTime.now(),0l);
        messages.save(message);
    }

    public void newReply(User from,Message message,String text){
        message.setMessage(text);
        message.setReply(message.getId());
        message.setDate(LocalDateTime.now());
        message.setFrom(from);
        messages.save(message);
    }

    /**
     * Return a message with a specific ID
     * @param id Long
     * @return Message
     */
    public Message findOne(Long id){
        return messages.findOne(id);
    }


    /**
     * Returns all messages for a specific chatroom
     * @param chatRoom ChatRoom
     * @return List<Message>
     */
    public List<Message> findAllForChatRoom(ChatRoom chatRoom){
        return StreamSupport.stream(messages.findAll().spliterator(),false).
                filter(x->{
                    return x.getTo() == chatRoom.getId();
                }).collect(Collectors.toList());
        }

    /**
     * Returns all messages received by logged user into a chat room
      * @param chatRoom
     * @param loggedUser
     * @return List<Message>
     */
    public  List<Message> findAllReceivedInChatroom(ChatRoom chatRoom,User loggedUser,LocalDateTime startDate, LocalDateTime endDate){
        return findAllForChatRoom(chatRoom).stream()
                .filter(x->!x.getFrom().equals(loggedUser))
                .filter(x->startDate.isBefore(x.getDate()) && endDate.isAfter(x.getDate()))
                .sorted((x,y)->x.getDate().compareTo(y.getDate()))
                .collect(Collectors.toList());
    }
}
