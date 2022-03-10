package com.example.socialnetworkgui;

import com.example.domain.User;
import com.example.domain.validators.ValidatorException;
import com.example.service.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MainController {
    private Network network;

    @FXML
    TextField textFieldUsername;
    @FXML
    TextField textFieldPassword;

    @FXML
    Button loginButton;
    @FXML
    Button exitButton;

    /**
     * sets the service
     * @param network
     */
    public void setService(Network network) {
        this.network = network;
    }

    /**
     * initializes the window
     */
    public void initialize(){}

    /**
     * handles the user button
     * @param ev
     * @throws IOException
     */
    @FXML
    public void handleLoginButton(ActionEvent ev) throws IOException {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();

        try{
            network.verifyPassword(username, password);
            User connectedUser = network.getUserByUsername(username);
            showUserWindow(connectedUser.getId());
        }
        catch (NoSuchElementException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("There is no user with the given username");
            alert.setContentText("Please give an existent username!");
            alert.showAndWait();
        }
        catch (ValidatorException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect password");
            alert.setContentText("Try again");
            alert.showAndWait();
        }
    }

    /**
     * shows the user window based on its id
     * @param id
     * @throws IOException
     */
    private void showUserWindow(Long id) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("user.fxml"));

        AnchorPane root=loader.load();

        UserController controller=loader.getController();
        controller.setUserId(id); // passing the logged user id
        controller.setService(network);

        Stage dialogStage = (Stage) loginButton.getScene().getWindow();
        dialogStage.setTitle("User");
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    /**
     * handles the exit button
     */
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}