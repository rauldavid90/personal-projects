package com.example.socialnetworkgui;

import com.example.domain.ChatRoom;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.service.ChatRoomService;
import com.example.service.MessageService;
import com.example.service.Network;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatwindowController {
    private Long loggedUser;
    private ChatRoom curentChatRoom;
    private ChatRoomService chatRoomService;
    private Network network;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    VBox chatWindow;
    @FXML
    Button backButton;
    @FXML
    Button sendButton;
    @FXML
    TextField textField;
    @FXML
    ScrollPane scrollPane;

    /**
     * Binding the controller with the services
     * @param network
     * @param loggedUser
     */
    public void setController(Network network,Long loggedUser, ChatRoom chatRoom){
        this.network = network;
        this.loggedUser = loggedUser;
        this.curentChatRoom = chatRoom;
        this.chatRoomService = network.getChatRoomService();
        chatRoomService.getAllMessagesForChatRoom(curentChatRoom).forEach(x->{addMessageToWindow(x);});
    }

    private int lengthLimit = 96;

    public void initialize(){
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    if(textField.getText().length()>= lengthLimit){
                        textField.setText(textField.getText().substring(0,lengthLimit));
                    }
                }
            }
        });
        chatWindow.heightProperty().addListener((observable -> {scrollPane.setVvalue(1.0);}));
    }

    /**
     * add Message to window
     * @param message
     */
    private void addMessageToWindow(Message message){
        Label newLabel = new Label("  "+message.getMessage());
        Label newDateLabel = new Label("  "+message.getFrom().getUsername()+" at "+message.getDate().format(formatter));
        Label spacingLabel = new Label();
        newLabel.setLayoutX(5);
        newDateLabel.setLayoutX(5);
        spacingLabel.setPrefWidth(930);
        newLabel.setPrefWidth(930);
        newDateLabel.setPrefWidth(930);
        newDateLabel.setAlignment(Pos.CENTER_LEFT);
        newLabel.setAlignment(Pos.CENTER_LEFT);
        if(message.getFrom().getId() == loggedUser){
            newLabel.setAlignment(Pos.CENTER_RIGHT);
            newDateLabel.setText(message.getDate().format(formatter));
            newDateLabel.setAlignment(Pos.CENTER_RIGHT);
        }
        chatWindow.getChildren().add(newLabel);
        chatWindow.getChildren().add(newDateLabel);
        chatWindow.getChildren().add(spacingLabel);
    }

    @FXML
    Pane chatPane;

    /**
     * send
     */
    public void send(){
        String text = textField.getText();
        textField.clear();
        if(text.equals("")) return;
        chatRoomService.sendMessage(curentChatRoom.getId(),network.getUserById(loggedUser),text);
        User user = new User("","","","");
        user.setId(loggedUser);
        Message newMessage = new Message(user,-1l,text, LocalDateTime.now(),0l);
        addMessageToWindow(newMessage);
    }

    /**
     * Open chatroomwindow
     */
    public void back(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chatroomwindow.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        }catch (IOException e){return;}

        ChatRoomController controller = loader.getController();
        controller.setLoggedUser(loggedUser);
        controller.setService(network);

        Stage dialogStage= (Stage) backButton.getScene().getWindow();
        dialogStage.setTitle("Chat Rooms");
        Scene scene=new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.show();
    }
}
