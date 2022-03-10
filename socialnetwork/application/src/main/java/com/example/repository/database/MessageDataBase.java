package com.example.repository.database;

import com.example.domain.Entity;
import com.example.domain.Friendship;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDataBase implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    Repository<Long,User> userRepo;

    public MessageDataBase(String url, String username, String password, Repository<Long,User> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    /**
     * saves a message in the database
     * @param entity message
     *         entity must be not null
     * @return
     */
    @Override
    public Message save(Message entity) {
        String sql="insert into Messages (fromuser, textmessage, date_message, time_message, reply, to_chatroom) values (?, ?, ?, ?, ?, ?)";
        //String sqlList="insert into messageuserlist (id_message, id_user) values (?, ?)";
        //String sqlGetId="select id from messages where textmessage=? and reply=? and date_message=? and time_message=?";
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){

            statement.setLong(1,entity.getFrom().getId());
            statement.setString(2,entity.getMessage());
            statement.setDate(3, Date.valueOf(entity.getDate().toLocalDate()));
            statement.setTime(4,Time.valueOf(entity.getDate().toLocalTime()));
            Long replied=entity.getReply();
            statement.setLong(5,replied);
            Long to = entity.getTo();
            statement.setLong(6,to);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

//        Long messageId=0l;
//        try(Connection connection= DriverManager.getConnection(url,username,password);
//            PreparedStatement statement=connection.prepareStatement(sqlGetId)){
//            Long replied=entity.getReply();
//            statement.setString(1,entity.getMessage());
//            statement.setLong(2,replied);
//            statement.setDate(3,Date.valueOf(entity.getDate().toLocalDate()));
//            statement.setTime(4,Time.valueOf(entity.getDate().toLocalTime()));
//            ResultSet resultSet=statement.executeQuery();
//            resultSet.next();
//            messageId=resultSet.getLong("id");
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }

//        try(Connection connection= DriverManager.getConnection(url,username,password);
//            PreparedStatement statement2=connection.prepareStatement(sqlList)){
//            statement2.setLong(1,messageId);
//            entity.getTo().forEach(x->{
//                try {
//                    statement2.setLong(2,x.getId());
//                    statement2.executeUpdate();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            });
//
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }

        return null;
    }

    @Override
    public Message delete(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    @Override
    public Message findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages where id = ?"))
        {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("fromuser");
                String text = resultSet.getString("textmessage");
                Date date = resultSet.getDate("date_message");
                Time time = resultSet.getTime("time_message");
                Long reply=resultSet.getLong("reply");
                Long to = resultSet.getLong("to_chatroom");

                User from = userRepo.findOne(idFrom);
                Message nouMessage=new Message(from,to,text,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()),reply);
                nouMessage.setId(id);
                return nouMessage;

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages=new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("fromuser");
                String text = resultSet.getString("textmessage");
                Date date = resultSet.getDate("date_message");
                Time time = resultSet.getTime("time_message");
                Long reply=resultSet.getLong("reply");
                Long to = resultSet.getLong("to_chatroom");

                User from = userRepo.findOne(idFrom);
                Message nouMessage=new Message(from,to,text,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()),reply);
                nouMessage.setId(id);
                messages.add(nouMessage);
            }
            return messages;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
