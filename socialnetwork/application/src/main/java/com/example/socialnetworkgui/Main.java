package com.example.socialnetworkgui;

import com.example.domain.User;
import com.example.domain.validators.*;
import com.example.repository.database.*;
import com.example.service.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main extends Application {
    public static  HostServices hs;


    @Override
    public void start(Stage stage) throws IOException {
        hs = getHostServices();

        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "postgres";

        UserService userService = new UserService(new UserDatabase(url,username, password), new UserValidator());
        FriendshipService friendshipService = new FriendshipService(new FriendshipDatabase(url,username,password), new FriendshipValidator());
        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(new FriendshipRequestDatabase(url, username, password), new FriendshipRequestValidator());
        MessageService messageService = new MessageService(new MessageDataBase(url,username, password, userService.getUsersRepo()));
        ChatRoomService chatRoomService = new ChatRoomService(new ChatRoomDatabase(url,username,password,userService.getUsersRepo()),messageService);
        EventService eventService = new EventService(new EventDatabase(url, username, password), new EventValidator());
        SubscriptionService subscriptionService = new SubscriptionService(new SubscriptionDatabase(url, username, password), new SubscriptionValidator());
        Network network = new Network(userService, friendshipService, friendshipRequestService, chatRoomService, eventService, subscriptionService);

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));

        AnchorPane root=loader.load();

        MainController controller=loader.getController();
        controller.setService(network);

        stage.initStyle(StageStyle.UNDECORATED); // removes the title bar
        stage.setScene(new Scene(root));
        stage.setTitle("Social Network");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}