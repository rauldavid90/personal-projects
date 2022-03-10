package com.example.service;

import com.example.domain.ChatRoom;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.Repository;
import com.example.utils.events.ObserverEvent;
import com.example.utils.events.EventType;
import com.example.utils.observer.Observable;
import com.example.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatRoomService implements Observable {
    private Repository<Long, ChatRoom> chatRoomsRepo;
    private MessageService messageService;
    private List<Observer> observerList = new ArrayList<>();

    public ChatRoomService(Repository<Long, ChatRoom> chatRoomsRepo, MessageService messageService) {
        this.chatRoomsRepo = chatRoomsRepo;
        this.messageService = messageService;
    }

    /**
     * Get all chatrooms which has the user as a member
     * @param user User
     * @return List<ChatRoom>
     */
    public List<ChatRoom> getChatroomsForUser(User user){
        List<ChatRoom> chatRoomsList = StreamSupport.stream(chatRoomsRepo.findAll().spliterator(),false)
                                            .collect(Collectors.toList());
        Predicate<ChatRoom> p = x->{return x.getMembers().contains(user);};
        chatRoomsList = chatRoomsList.stream().filter(p).collect(Collectors.toList());
        return chatRoomsList;
    }

    /**
     * Returns all messages coresponding to a chatroom in chronological order
     * @param chatRoom ChatRoom
     * @return List<Message>
     */
    public List<Message> getAllMessagesForChatRoom(ChatRoom chatRoom){
        return messageService.findAllForChatRoom(chatRoom).stream().sorted((x,y)->{return x.getDate().compareTo(y.getDate());}).collect(Collectors.toList());
    }

    /**
     * Return the chatroom between two given users.
     * @param user1
     * @param user2
     * @return ChatRoom
     */
    public ChatRoom findChatRoomForUsers(User user1, User user2){
        List<ChatRoom> lista = StreamSupport.stream(chatRoomsRepo.findAll().spliterator(),false)
                .filter(x->x.getMembers().contains(user1) && x.getMembers().contains(user2) && x.getMembers().size()==2)
                .collect(Collectors.toList());
        if (lista.size()==0) return null;
        else return lista.get(0);
    }

    /**
     * Send a new message
     * @param to ID of chatroom
     * @param fromUser User who sent the message
     * @param text String Message text
     */
    public void sendMessage(Long to,User fromUser,String text){
        messageService.newMessage(fromUser,to,text);
    }

    public void createNewChatRoom(List<User> members, String name) throws Exception {
        ChatRoom chatRoom = new ChatRoom(members,name);
        List<ChatRoom> chatRooms = StreamSupport.stream(chatRoomsRepo.findAll().spliterator(),false).collect(Collectors.toList());
        boolean ok = true;
        for(ChatRoom chat:chatRooms){
            for(User user: chat.getMembers()){
                if(members.contains(user)){}
                else ok = false;
            }
        }
        if(ok) {
            if(!chatRooms.isEmpty()) throw new Exception("Already exists a chatroom with this members!");}
        chatRoomsRepo.save(chatRoom);
        notifyObservers(new ObserverEvent(EventType.NEW_CHATROOM));
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObservers(ObserverEvent observerEvent) {
        observerList.forEach(x->x.update(observerEvent));
    }

    @Override
    public void removeObservers(){
        observerList.clear();
    }

    public List<Message> getMessagesReceived(ChatRoom chatRoom, User logged, LocalDateTime startDate, LocalDateTime endDate){
        return messageService.findAllReceivedInChatroom(chatRoom, logged, startDate, endDate);
    }
}
