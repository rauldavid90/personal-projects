package com.example.socialnetworkgui;

import com.example.domain.ChatRoom;
import com.example.domain.User;
import com.example.service.Network;
import com.example.utils.events.ObserverEvent;
import com.example.utils.events.EventType;
import com.example.utils.observer.Observer;
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

public class ChatRoomController implements Observer {
    private Network network;
    private Long userId;

    ObservableList<ChatRoom>  chatRooms = FXCollections.observableArrayList();
    ObservableList<User> friends = FXCollections.observableArrayList();

    @FXML
    TableColumn<ChatRoom, String> tableColumnName;
    @FXML
    TableView<ChatRoom> tableViewChatRooms;
    @FXML
    Button backButton;
    @FXML
    Button openButton;
    @FXML
    TableView tableViewFriends;
    @FXML
    TableColumn tableColumnUserName;
    @FXML
    TableColumn tableColumnFirstName;
    @FXML
    TableColumn tableColumnLastName;
    @FXML
    TextField textFieldName;

    /**
     * Sets controller service
     * @param network
     */
  public void setService(Network network){
      this.network = network;
      initModel();
      network.getChatRoomService().addObserver(this);
      }

  private void initModel(){
      try {
          chatRooms.setAll(network.getChatRoomService().getChatroomsForUser(network.getUserService().get(userId)));
      }catch (Exception e){}
      try{
          friends.setAll(network.getFriendsForUser(userId));
      }catch (Exception e){}
  }

    /**
     * Sets logged user id
     * @param id
     */
  public void setLoggedUser(Long id){
      this.userId = id;
  }

  public void initialize(){
      tableColumnName.setCellValueFactory(new PropertyValueFactory<ChatRoom,String>("name"));
      tableViewChatRooms.setItems(chatRooms);

      tableColumnUserName.setCellValueFactory(new PropertyValueFactory<User, Long>("username"));
      tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
      tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
      tableViewFriends.setItems(friends);
      tableViewFriends.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public void back(){
      removeObservers();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("user.fxml"));
      AnchorPane root;
      try {
          root = loader.load();
      }catch (IOException e){return;}

      UserController controller = loader.getController();
      controller.setUserId(userId);
      controller.setService(network);

      Stage dialogStage= (Stage) backButton.getScene().getWindow();
      dialogStage.setTitle("Chat Rooms");
      Scene scene=new Scene(root);
      dialogStage.setScene(scene);

      dialogStage.show();
  }

    /**
     * open chat window
     */
  public void openChatWindow(){
    ObservableList<ChatRoom> selectedItems = tableViewChatRooms.getSelectionModel().getSelectedItems();
    try{
        loadChatWindow(selectedItems.get(0));
    }
    catch (IndexOutOfBoundsException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        alert.setTitle("Error");
        alert.setHeaderText("No selected chatroom");
        alert.setContentText("Please select a chatroom");
        alert.showAndWait();
    }

  }

  private void removeObservers(){
      network.getChatRoomService().removeObservers();
  }

    /**
     * load chat window
     * @param chatRoom
     */
  private void loadChatWindow(ChatRoom chatRoom){
      removeObservers();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("chatwindow.fxml"));
      AnchorPane root;
      try {
          root = loader.load();
      }catch (IOException e){
          e.printStackTrace();
          return;}

      ChatwindowController controller = loader.getController();
      controller.setController(network,userId,chatRoom);

      Stage dialogStage= (Stage) openButton.getScene().getWindow();
      dialogStage.setTitle(chatRoom.getName());
      Scene scene=new Scene(root);
      dialogStage.setScene(scene);
      dialogStage.show();
  }

    /**
     * Handle createButton. Creates a new chatroom with the selected friends as members
     */
  public void createChatRoom(){
      String name = textFieldName.getText();
      ObservableList<User> membersList = tableViewFriends.getSelectionModel().getSelectedItems();
      textFieldName.clear();
        if(membersList.size()==0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("ChatRoomError");
            alert.setContentText("Members must be chosen");
            alert.showAndWait();
            tableViewFriends.getSelectionModel().clearSelection();
            return;
        }
        if(name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("ChatRoomError");
            alert.setContentText("Chat room name cannot be empty!");
            alert.showAndWait();
            tableViewFriends.getSelectionModel().clearSelection();
            return;
        }
      List<User> members = new ArrayList<>();
      tableViewFriends.getSelectionModel().getSelectedItems().forEach(x->members.add((User)x));
      members.add(network.getUserById(userId));
      try {
          network.getChatRoomService().createNewChatRoom(members, name);
      }catch (Exception e){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          DialogPane dialogPane = alert.getDialogPane();
          dialogPane.getStylesheets().add(
                  Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
          alert.setTitle("Error");
          alert.setHeaderText("ChatRoomError");
          alert.setContentText(e.getMessage());
          alert.showAndWait();
          tableViewFriends.getSelectionModel().clearSelection();
      }
  }

    @Override
    public void update(ObserverEvent observerEvent) {
        if (observerEvent.getType() == EventType.NEW_CHATROOM){
            try {
                chatRooms.setAll(network.getChatRoomService().getChatroomsForUser(network.getUserById(userId)));
            }catch (Exception e){}
        }
    }
}
