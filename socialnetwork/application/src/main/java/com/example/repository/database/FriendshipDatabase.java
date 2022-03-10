package com.example.repository.database;

import com.example.domain.Friendship;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDatabase implements Repository<Long, Friendship> {
    private String url;
    private String username;
    private String password;

    /**
     * constructor
     * @param url
     * @param username
     * @param password
     */
    public FriendshipDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * saves a friendship in the database
     * @param entity user
     *         entity must be not null
     * @return
     */
    @Override
    public Friendship save(Friendship entity) {

        String sql = "insert into Friendships (id_of_friend_1, id_of_friend_2, friendship_date, friendship_time ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getId_friend_1());
            statement.setLong(2, entity.getId_friend_2());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.setTime(4, Time.valueOf(entity.getTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deletes a friendship from the data base
     * @param aLong id
     * @return
     */
    @Override
    public Friendship delete(Long aLong) {

        String sql = "delete from Friendships where id = ?";

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
     * updates an user in the database
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public Friendship update(Friendship entity) {

        String sql = "update Friendships set id_of_friend_1 = ?, id_of_friend_2 = ?, friendship_date=?, friendship_time=? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getId_friend_1());
            statement.setLong(2, entity.getId_friend_2());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.setTime(4, Time.valueOf(entity.getTime()));
            statement.setLong(5, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds one friendship in the database table
     * @param aLong id
     * @return found friendship, null else
     */
    @Override
    public Friendship findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships where id = ?")) {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long id_of_friend_1 = resultSet.getLong("id_of_friend_1");
                Long id_of_friend_2 = resultSet.getLong("id_of_friend_2");
                Date date = resultSet.getDate("friendship_date");
                Time time = resultSet.getTime("friendship_time");

                Friendship friendship = new Friendship(id_of_friend_1, id_of_friend_2, LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendship.setId(id);
                return friendship;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the list of friendships from the database table
     * @return
     */
    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long id_friend_1 = resultSet.getLong("id_of_friend_1");
                Long id_friend_2 = resultSet.getLong("id_of_friend_2");
                Date date = resultSet.getDate("friendship_date");
                Time time = resultSet.getTime("friendship_time");

                Friendship friendship = new Friendship(id_friend_1, id_friend_2,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }

    /**
     * returns a boolean based on friendships table emptiness
     * @return
     */
    @Override
    public boolean isEmpty() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long id_friend_1 = resultSet.getLong("id_of_friend_1");
                Long id_friend_2 = resultSet.getLong("id_of_friend_2");
                Date date = resultSet.getDate("friendship_date");
                Time time = resultSet.getTime("friendship_time");

                Friendship friendship = new Friendship(id_friend_1, id_friend_2,LocalDateTime.of(date.toLocalDate(),time.toLocalTime()));
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships.isEmpty();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return friendships.isEmpty();
    }
}
