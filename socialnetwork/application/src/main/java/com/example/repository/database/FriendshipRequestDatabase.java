package com.example.repository.database;

import com.example.domain.Friendship;
import com.example.domain.FriendshipRequest;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipRequestDatabase implements Repository<Long, FriendshipRequest> {
    private String url;
    private String username;
    private String password;

    /**
     * constructor
     * @param url
     * @param username
     * @param password
     */
    public FriendshipRequestDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * saves a friendship request in the database
     * @param entity user
     *         entity must be not null
     * @return
     */
    @Override
    public FriendshipRequest save(FriendshipRequest entity) {

        String sql = "insert into friendship_requests (from_id, to_id, status, request_date, request_time,to_username,from_username) values (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getFrom_id());
            statement.setLong(2, entity.getTo_id());
            statement.setString(3, entity.getStatus());
            LocalDateTime date=entity.getDate();
            statement.setDate(4, Date.valueOf(date.toLocalDate()));
            statement.setTime(5, Time.valueOf(date.toLocalTime()));
            statement.setString(6,entity.getTo_username());
            statement.setString(7,entity.getFrom_username());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deletes a friendship request from the database
     * @param aLong id
     * @return
     */
    @Override
    public FriendshipRequest delete(Long aLong) {

        String sql = "delete from friendship_requests where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, aLong);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * updates an friendship request in the database
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public FriendshipRequest update(FriendshipRequest entity) {

        String sql = "update friendship_requests set to_id = ?, from_id = ?, status = ?, request_date = ?, request_time = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getTo_id());
            statement.setLong(2, entity.getFrom_id());
            statement.setString(3, entity.getStatus());
            statement.setDate(4,Date.valueOf(entity.getDate().toLocalDate()));
            statement.setTime(5,Time.valueOf(entity.getDate().toLocalTime()));
            statement.setLong(6, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds one friendship request in the database table
     * @param aLong id
     * @return found friendship request, null else
     */
    @Override
    public FriendshipRequest findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendship_requests where id = ?")) {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long to_id = resultSet.getLong("to_id");
                Long from_id = resultSet.getLong("from_id");
                String status = resultSet.getString("status");
                Date date= resultSet.getDate("request_date");
                Time time=resultSet.getTime("request_time");
                String to_username = resultSet.getString("to_username");
                String from_username = resultSet.getString("from_username");

                FriendshipRequest friendshipRequest = new FriendshipRequest(from_id, to_id,to_username,from_username, status,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendshipRequest.setId(id);
                return friendshipRequest;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the list of friendship requests from the database table
     * @return
     */
    @Override
    public Iterable<FriendshipRequest> findAll() {
        Set<FriendshipRequest> friendshipRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendship_requests");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long to_id = resultSet.getLong("to_id");
                Long from_id = resultSet.getLong("from_id");
                String status = resultSet.getString("status");
                Date date= resultSet.getDate("request_date");
                Time time=resultSet.getTime("request_time");
                String to_username = resultSet.getString("to_username");
                String from_username = resultSet.getString("from_username");

                FriendshipRequest friendshipRequest = new FriendshipRequest(from_id, to_id,to_username,from_username, status,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendshipRequest.setId(id);
                friendshipRequests.add(friendshipRequest);
            }
            return friendshipRequests;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return friendshipRequests;
    }

    /**
     * returns a boolean based on friendship requests table emptiness
     * @return
     */
    @Override
    public boolean isEmpty() {
        Set<FriendshipRequest> friendshipRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendship_requests");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long to_id = resultSet.getLong("to_id");
                Long from_id = resultSet.getLong("from_id");
                String status = resultSet.getString("status");
                Date date= resultSet.getDate("request_date");
                Time time=resultSet.getTime("request_time");
                String to_username = resultSet.getString("to_username");
                String from_username = resultSet.getString("from_username");


                FriendshipRequest friendshipRequest = new FriendshipRequest(from_id, to_id,to_username,from_username, status,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendshipRequest.setId(id);
                friendshipRequests.add(friendshipRequest);
            }
            return friendshipRequests.isEmpty();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return friendshipRequests.isEmpty();
    }
}
