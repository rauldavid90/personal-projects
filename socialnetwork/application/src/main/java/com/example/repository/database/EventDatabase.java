package com.example.repository.database;

import com.example.domain.Event;
import com.example.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class EventDatabase implements Repository<Long, Event> {
    private String url;
    private String username;
    private String password;
    private int nrOfElems = 0;

    /**
     * constructor
     * @param url
     * @param username
     * @param password
     */
    public EventDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }



    /**
     * saves an event in the database
     * @param entity
     *         entity must be not null
     * @return
     */
    @Override
    public Event save(Event entity) {
        String sql = "insert into events (name, description, date, time) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setDate(3, Date.valueOf(entity.getDate()));
            statement.setTime(4, Time.valueOf(entity.getTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * deletes an event from the database
     * @param aLong
     * @return
     */
    @Override
    public Event delete(Long aLong) {
        return null;
    }

    /**
     * updates an event in the database
     * @param entity
     *          entity must not be null
     * @return
     */
    @Override
    public Event update(Event entity) {
        return null;
    }

    /**
     * finds one event in the database
     * @param aLong
     * @return
     */
    @Override
    public Event findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM events where id = ?")) {

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");

                Event event = new Event(name, description, date.toLocalDate(), time.toLocalTime());
                event.setId(id);
                return event;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * finds all the events in the database
     * @return
     */
    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM events order by id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Date date= resultSet.getDate("date");
                Time time=resultSet.getTime("time");

                Event event = new Event(name, description, date.toLocalDate(), time.toLocalTime());
                event.setId(id);
                events.add(event);
            }
            return events;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return events;
    }

    /**
     * verifies if the event database is empty
     * @return
     */
    @Override
    public boolean isEmpty() {
        return false;
    }
}
