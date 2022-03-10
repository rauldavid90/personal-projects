package com.example.socialnetworkgui;

import com.example.domain.User;
import com.example.repository.exceptions.RepositoryException;
import com.example.service.Network;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NewFriendController {
    private Long userId;
    private Network network;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private List<User> extraUserList;

    @FXML
    TableColumn<User,String> tableColumnId;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableView<User> tableViewUsers;

    @FXML
    TextField textFieldFilter;

    @FXML
    Button backButton;

    /**
     * Set the network.
     * @param network
     */
    public void setService(Network network,Long userId){
        this.network=network;
        this.userId=userId;
        extraUserList = network.getAllUserWhichAreNotFriendsWithUser(userId);
        try {
            users.setAll(extraUserList);
        }catch (Exception e){}
    }

    /**
     * initializes the window
     */
    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        tableViewUsers.setItems(users);

        textFieldFilter.textProperty().addListener(o->handleFilter());
    }

    /**
     * filter users by their first and last name
     */
    private void handleFilter(){
        Predicate<User> lastNamePredicate = user -> user.getLastName().startsWith(textFieldFilter.getText());
        Predicate<User> firstNamePredicate = user -> user.getFirstName().startsWith(textFieldFilter.getText());
        try {
            users.setAll(extraUserList
                    .stream()
                    .filter(lastNamePredicate.or(firstNamePredicate))
                    .collect(Collectors.toList()));
        }catch (Exception e){}
    }

    /**
     * send a friend request to the selected user
     */
    public void sendRequest(){
        ObservableList<User> selectedItems=tableViewUsers.getSelectionModel().getSelectedItems();

        try {
            network.addFriendshipRequest(userId, selectedItems.get(0).getId(), "pending");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Sent");
            alert.setContentText("Friendship request sent successfully");
            alert.showAndWait();
        }
        catch (RepositoryException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("There is already a friendship request");
            alert.setContentText("Please choose another user for a friendship request!");
            alert.showAndWait();
        }
        catch (IndexOutOfBoundsException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected user");
            alert.setContentText("Please select an user");
            alert.showAndWait();
        }
    }

    /**
     * close the window
     */
    public void back(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("user.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        }catch (IOException e){return;}

        UserController controller=loader.getController();
        controller.setUserId(userId); // passing the logged user id
        controller.setService(network);

        stage.setTitle("User");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
}
