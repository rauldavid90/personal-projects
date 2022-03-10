package com.example.repository.database;

import com.example.domain.ChatRoom;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ChatRoomDatabase implements Repository<Long, ChatRoom> {
    private String url;
    private String username;
    private String password;
    Repository<Long, User> userRepo;

    public ChatRoomDatabase(String url, String username, String password, Repository<Long, User> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    @Override
    public ChatRoom save(ChatRoom entity) {
        String sql="insert into chatrooms (cname) values (?)";
        String sqlList="insert into chatroomsuserlist (id_chatroom, id_user) values (?, ?)";
        String sqlGetId="select id_chatroom from chatrooms where cname=?";
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sql)){

            statement.setString(1,entity.getName());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

        Long chatRoomId=0l;
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement=connection.prepareStatement(sqlGetId)){

            statement.setString(1,entity.getName());
            ResultSet resultSet=statement.executeQuery();
            resultSet.next();
            chatRoomId = resultSet.getLong("id_chatroom");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement2=connection.prepareStatement(sqlList)){
            statement2.setLong(1,chatRoomId);
            entity.getMembers().forEach(x->{
                try {
                    statement2.setLong(2,x.getId());
                    statement2.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public ChatRoom delete(Long aLong) {
        return null;
    }

    @Override
    public ChatRoom update(ChatRoom entity) {
        return null;
    }

    @Override
    public ChatRoom findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<ChatRoom> findAll() {
        Set<ChatRoom> chatrooms=new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM chatrooms");
             PreparedStatement statement1 = connection.prepareStatement("select * from chatroomsuserlist where id_chatroom=?")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id_chatroom");
                String name = resultSet.getString("cname");

                statement1.setLong(1,id);
                ResultSet listaUseri=statement1.executeQuery();
                List<User> lista=new ArrayList<>();
                while(listaUseri.next()){
                    Long idUser=listaUseri.getLong("id_user");
                    User nouUser=userRepo.findOne(idUser);
                    lista.add(nouUser);
                }

                ChatRoom newChatRoom = new ChatRoom(lista,name);
                newChatRoom.setId(id);
                chatrooms.add(newChatRoom);
            }
            return chatrooms;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return chatrooms;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
