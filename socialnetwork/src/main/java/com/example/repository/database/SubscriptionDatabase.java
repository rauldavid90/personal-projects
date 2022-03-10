package com.example.repository.database;

import com.example.domain.Subscription;
import com.example.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionDatabase implements Repository<Long, Subscription> {
    private String url;
    private String username;
    private String password;

    /**
     * constructor
     * @param url
     * @param username
     * @param password
     */
    public SubscriptionDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * saves an subscription
     * @param entity
     *         entity must be not null
     * @return
     */
    @Override
    public Subscription save(Subscription entity) {
        String sql = "insert into subscriptions (user_id, event_id, event_name, notifications_status) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getUser_id());
            statement.setLong(2, entity.getEvent_id());
            statement.setString(3, entity.getEvent_name());
            statement.setString(4, entity.getNotifications_status());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * deletes a subscription
     * @param aLong
     * @return
     */
    @Override
    public Subscription delete(Long aLong) {
        String sql = "delete from subscriptions where id = ?";

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
     * updates a subscription
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public Subscription update(Subscription entity) {
        String sql = "update subscriptions set user_id = ?, event_id = ?, event_name = ?, notifications_status = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getUser_id());
            statement.setLong(2, entity.getEvent_id());
            statement.setString(3, entity.getEvent_name());
            statement.setString(4, entity.getNotifications_status());
            statement.setLong(5, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds a subscription
     * @param aLong
     * @return
     */
    @Override
    public Subscription findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM subscriptions where id = ?")) {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long user_id = resultSet.getLong("user_id");
                Long event_id = resultSet.getLong("event_id");
                String event_name = resultSet.getString("event_name");
                String notifications_status = resultSet.getString("notifications_status");

                Subscription subscription = new Subscription(user_id, event_id, event_name, notifications_status);
                subscription.setId(id);
                return subscription;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds all subscriptions
     * @return
     */
    @Override
    public Iterable<Subscription> findAll() {
        Set<Subscription> subscriptions = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM subscriptions");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                Long user_id = resultSet.getLong("user_id");
                Long event_id = resultSet.getLong("event_id");
                String event_name = resultSet.getString("event_name");
                String notifications_status = resultSet.getString("notifications_status");

                Subscription subscription = new Subscription(user_id, event_id, event_name, notifications_status);
                subscription.setId(id);
                subscriptions.add(subscription);
            }
            return subscriptions;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return subscriptions;
    }

    /**
     * verifies if the subscription list is empty
     * @return
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
}
