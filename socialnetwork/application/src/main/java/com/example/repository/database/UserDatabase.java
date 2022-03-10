package com.example.repository.database;

import com.example.domain.User;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;
import com.example.utils.hashAlgorithm.MD5;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDatabase implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;

    /**
     * constrcutor
     * @param url
     * @param username
     * @param password
     */
    public UserDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * saves an user in the database
     * @param entity user
     *         entity must be not null
     * @return
     */
    @Override
    public User save(User entity) {

        String sql = "insert into users (first_name, last_name, username, password) values (?, ?, ?, ?)";
        String hashedPass = MD5.getHash(entity.getPassword());
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            statement.setString(4, hashedPass);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deletes an user from the data base
     * @param aLong id
     * @return
     */
    @Override
    public User delete(Long aLong) {

        String sql = "delete from users where id = ?";

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
     * updates an user in the data base
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public User update(User entity) {

        String sql = "update users set first_name = ?, last_name = ? , username = ?, password = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            statement.setString(4, entity.getPassword());
            statement.setLong(5, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds one user in the database table
     * @param aLong id
     * @return
     */
    @Override
    public User findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users where id = ?")) {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(first_name, last_name, username, password);
                user.setId(id);

                return user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the list of users from the database table
     * @return
     */
    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(first_name, last_name, username, password);
                user.setId(id);

                users.add(user);
            }
            return users;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    /**
     * returns a boolean based on users table emptiness
     * @return
     */
    @Override
    public boolean isEmpty() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(first_name, last_name, username, password);
                user.setId(id);

                users.add(user);
            }
            return users.isEmpty();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users.isEmpty();
    }
}
