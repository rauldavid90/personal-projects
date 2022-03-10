package com.example.socialnetworkgui;


import com.example.domain.Event;
import com.example.domain.FriendshipRequest;
import com.example.domain.Subscription;
import com.example.domain.User;
import com.example.domain.validators.ValidatorException;
import com.example.repository.exceptions.RepositoryException;
import com.example.service.Network;
import com.example.utils.PageEvent;
import com.example.utils.events.ObserverEvent;
import com.example.utils.events.EventType;
import com.example.utils.observer.Observer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserController implements Observer {
    private Network network;
    private Long userId;

    ObservableList<User> friends = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> sentFriendshipRequests = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> receivedFriendshipRequests = FXCollections.observableArrayList();
    ObservableList<Event> events = FXCollections.observableArrayList();
    ObservableList<Subscription> userSubscriptions = FXCollections.observableArrayList();

    @FXML Label loggedUser;

    @FXML GridPane paneEvents;
    @FXML GridPane paneFriendRequests;
    @FXML GridPane paneFriends;
    @FXML GridPane paneMessages;

    @FXML Button btnFriends;
    @FXML Button btnFriendRequests;
    @FXML Button btnMessages;
    @FXML Button btnEvents;
    @FXML Button btnLogout;

    @FXML TableColumn<User, Long> tableColumnId;
    @FXML TableColumn<User, String> tableColumnFirstName;
    @FXML TableColumn<User, String> tableColumnLastName;
    @FXML TableView<User> tableViewFriends;

    @FXML TableColumn<FriendshipRequest, String> tableColumnIdFrom_Received;
    @FXML TableColumn<FriendshipRequest, String> tableColumnIdTo_Received;
    @FXML TableColumn<FriendshipRequest, String> tableColumnStatus_Received;
    @FXML TableColumn<FriendshipRequest, LocalDateTime> tableColumnDate_Received;
    @FXML TableView<FriendshipRequest> tableViewFriendshipRequests_Received;

    @FXML TableColumn<FriendshipRequest, String> tableColumnIdFrom_Sent;
    @FXML TableColumn<FriendshipRequest, String> tableColumnIdTo_Sent;
    @FXML TableColumn<FriendshipRequest, String> tableColumnStatus_Sent;
    @FXML TableColumn<FriendshipRequest, LocalDateTime> tableColumnDate_Sent;
    @FXML TableView<FriendshipRequest> tableViewFriendshipRequests_Sent;

    @FXML TableColumn<Event, String> tableColumnEventName;
    @FXML TableColumn<Event, String> tableColumnEventDescription;
    @FXML TableColumn<Event, LocalDate> tableColumnEventDate;
    @FXML TableColumn<Event, LocalTime> tableColumnEventTime;
    @FXML TableView<Event> tableViewEvents;
    @FXML Button btnAddEvent;
    @FXML TextField textFieldEventName;
    @FXML TextField textFieldEventDescription;
    @FXML DatePicker datePickerEventDate;
    @FXML TextField textFieldEventTime;
    @FXML Button btnSubscribe;
    @FXML Button btnUnsubscribe;
    @FXML Button btnSwitchNotifications;
    @FXML
    TableColumn<Subscription, String> tableColumnSubscriptionEventName;
    @FXML TableColumn<Subscription, String> tableColumnSubscriptionNotifications;
    @FXML TableView<Subscription> tableViewSubscriptions;
    @FXML Label lblNotifications;

    @FXML Button removeFriendButton;
    @FXML Button sendFriendshipRequestButton;
    @FXML Button deleteFriendshipRequestButton;
    @FXML Button approveFriendshipRequestButton;
    @FXML Button rejectFriendshipRequestButton;
    @FXML Button exitButton;
    @FXML Button chatHistoryButton;
    @FXML Button historyButton;
    @FXML DatePicker checkIn;
    @FXML DatePicker checkOut;

    @FXML Button btnPreviousPage;
    @FXML Button btnNextPage;

    /**
     * sets the service
     * @param network
     */
    public void setService(Network network){
        this.network = network;
        loggedUser.setText(network.getUserById(userId).getFirstName() + " " + network.getUserById(userId).getLastName());
        setView();
        network.getFriendshipService().addObserver(this);
        network.getFriendshipRequestService().addObserver(this);
        network.getEventService().addObserver(this);
        network.getSubscriptionService().addObserver(this);
    }

    /**
     * next page
     */
    public void nextPage(){
        try{
        events.setAll(network.getNextPage());
        }catch (Exception e){
        }
    }

    /**
     * previous page
     */
    public void previousPage(){
        try{
            events.setAll(network.getPreviousPage());
        }catch (Exception e){
        }
    }

    /**
     * next page friend
     */
    public void nextPageFriend(){
        try{
            friends.setAll(network.getPageFriends(PageEvent.NEXT_PAGE,userId));
        }catch (Exception e){
        }
    }

    /**
     * previous pgae friend
     */
    public void previousPageFriend(){
        try{
            friends.setAll(network.getPageFriends(PageEvent.PREVIOUS_PAGE,userId));
        }catch (Exception e){
        }
    }

    /**
     * sets the logged in user id
     * @param id
     */
    public void setUserId(Long id) {
        this.userId = id;
    }

    /**
     * Set rows in friends table
     */
    public void setView(){
        try {
            receivedFriendshipRequests.setAll(network.getAllFriendshipRequestsReceivedByUserId(userId));
        }catch (Exception e){}
        try {
            sentFriendshipRequests.setAll(network.getAllFriendshipRequestsSentByUserId(userId));
        }catch (Exception e){}
        try {
            events.setAll(network.getCurrentPage());
        }catch (Exception e){}
        try{
            friends.setAll(network.getPageFriends(PageEvent.CURRENT_PAGE,userId));
        }catch (Exception e){}
        try{
        userSubscriptions.setAll(network.getAllSubscriptionsForUserId(userId));
        }catch (Exception e){
        }
        }

    /**
     * initializes the window
     */
    public void initialize(){

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableViewFriends.setItems(friends);

        tableColumnIdFrom_Sent.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("from_username"));
        tableColumnIdTo_Sent.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("to_username"));
        tableColumnStatus_Sent.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("status"));
        tableColumnDate_Sent.setCellValueFactory(new PropertyValueFactory<FriendshipRequest,LocalDateTime>("date"));
        tableViewFriendshipRequests_Sent.setItems(sentFriendshipRequests);
        tableViewFriendshipRequests_Sent.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableColumnIdFrom_Received.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("from_username"));
        tableColumnIdTo_Received.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("to_username"));
        tableColumnStatus_Received.setCellValueFactory(new PropertyValueFactory<FriendshipRequest, String>("status"));
        tableColumnDate_Received.setCellValueFactory(new PropertyValueFactory<FriendshipRequest,LocalDateTime>("date"));
        tableViewFriendshipRequests_Received.setItems(receivedFriendshipRequests);
        tableViewFriendshipRequests_Received.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableColumnEventName.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
        tableColumnEventDescription.setCellValueFactory(new PropertyValueFactory<Event, String>("description"));
        tableColumnEventDate.setCellValueFactory(new PropertyValueFactory<Event, LocalDate>("date"));
        tableColumnEventTime.setCellValueFactory(new PropertyValueFactory<Event, LocalTime>("time"));
        tableViewEvents.setItems(events);
        tableViewEvents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableColumnSubscriptionEventName.setCellValueFactory(new PropertyValueFactory<Subscription, String>("event_name"));
        tableColumnSubscriptionNotifications.setCellValueFactory(new PropertyValueFactory<Subscription, String>("notifications_status"));
        tableViewSubscriptions.setItems(userSubscriptions);
        tableViewSubscriptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        showNotifications();

        checkIn.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate deComparat = LocalDate.now();
                deComparat = deComparat.minusDays(1);
                setDisable(empty || date.compareTo(deComparat) > 0 );
            }
        });

        checkOut.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });
        datePickerEventDate.setDayCellFactory(param-> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
    }

    /**
     * handles the show friends pane button
     */
    public void showFriendsPane(){
        paneFriends.toFront();
    }

    /**
     * delete the selected friend from the table
     */
    public void removeFriend(){
        ObservableList<User> selectedItem = tableViewFriends.getSelectionModel().getSelectedItems();
        try{
            network.deleteFriendshipforUsers(userId,selectedItem.get(0).getId());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected friend");
            alert.setContentText("Please select a friend");
            alert.showAndWait();
        }
    }

    /**
     * Return file save location
     * @return
     */
    private String getSaveLocation(){
        String fileLocation = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Choose save location");
        // fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            fileLocation=fileChooser.getSelectedFile().toString();
        }
        return fileLocation;
    }

    /**
     * Handle chatHistoryButton
     */
    public void createChatHistoryReport(){
        ObservableList<User> lista = tableViewFriends.getSelectionModel().getSelectedItems();
        if(lista.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No user selected");
            alert.setContentText("Please select one friend to create a report");
            alert.showAndWait();
            return;
        }
        User selectedUser = lista.get(0);
        if(checkIn.getEditor().getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No date selected");
            alert.setContentText("Please select start date");
            alert.showAndWait();
            return;

        }
        if(checkOut.getEditor().getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No date selected");
            alert.setContentText("Please select end date!!!");
            alert.showAndWait();
            return;
        }
        LocalDateTime startDate = checkIn.getValue().atStartOfDay();
        LocalDateTime endDate = checkOut.getValue().atTime(23,59,59);
        if (startDate.isAfter(endDate)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Date error");
            alert.setContentText("Start date must be before end date");
            alert.showAndWait();
            return;
        }
        String string="";
        try {
            string = network.createChatHistoryReport(network.getUserById(userId), selectedUser, startDate, endDate, "a.pdf");
        }
        catch (Exception e){}

        Alert alertPreview = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPanePreview = alertPreview.getDialogPane();
        dialogPanePreview.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        alertPreview.setTitle("PDF preview");
        alertPreview.setHeaderText("Do you want to save this content in pdf format?");
        alertPreview.setContentText(string);
        alertPreview.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alertPreview.showAndWait();
        if(result.get()==ButtonType.OK) {}
        else {return;}


        String fileLocation = getSaveLocation();
        if(fileLocation.equals("")){ return; }
        fileLocation += ".pdf";
        try {
            network.createChatHistoryReport(network.getUserById(userId), selectedUser, startDate, endDate, fileLocation);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Succes");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Report created successful");
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        tableViewFriends.getSelectionModel().clearSelection();
        checkIn.getEditor().clear();
        checkOut.getEditor().clear();
    }

    /**
     * Handle historyButton
     */
    public void createHistoryReport(){

        if(checkIn.getEditor().getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No date selected");
            alert.setContentText("Please select start date");
            alert.showAndWait();
            return;

        }
        if(checkOut.getEditor().getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No date selected");
            alert.setContentText("Please select end date!!!");
            alert.showAndWait();
            return;
        }
        LocalDateTime startDate = checkIn.getValue().atStartOfDay();
        LocalDateTime endDate = checkOut.getValue().atTime(23,59,59);
        if (startDate.isAfter(endDate)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Date error");
            alert.setContentText("Start date must be before end date");
            alert.showAndWait();
            return;
        }
        String string="";
        try {
            string = network.createHistoryReport(network.getUserById(userId), startDate, endDate, "a.pdf");
        }
        catch (Exception e){}

        Alert alertPreview = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPanePreview = alertPreview.getDialogPane();
        dialogPanePreview.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        alertPreview.setTitle("PDF preview");
        alertPreview.setHeaderText("Do you want to save this content in pdf format?");
        alertPreview.setContentText(string);
        alertPreview.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alertPreview.showAndWait();
        if(result.get()==ButtonType.OK) {}
        else {return;}

        String fileLocation = getSaveLocation();
        if(fileLocation.equals("")){ return;}
        fileLocation += ".pdf";

        try {

            network.createHistoryReport(network.getUserById(userId), startDate, endDate, fileLocation);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Succes");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Report created successful");
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        tableViewFriends.getSelectionModel().clearSelection();
        checkIn.setValue(null);
        checkOut.setValue(null);
    }

    /**
     * handles the show friend requests pane button
     */
    public void showFriendRequestPane(){
        paneFriendRequests.toFront();
    }

    /**
     * handles the send friend request button
     */
    public void sendFriendshipRequest() throws IOException {
        showAddNewFriendWindow();
    }

    /**
     * show the newFriend window
     */
    private void showAddNewFriendWindow() throws IOException {
        removeObservers();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newfriend.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        }catch (IOException e){return;}

        NewFriendController controller = loader.getController();
        controller.setService(network,userId);

        Stage dialogStage= (Stage) sendFriendshipRequestButton.getScene().getWindow();
        dialogStage.setTitle("Adding a new friend");
        Scene scene=new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.show();
    }

    /**
     * handles the delete friend request button
     */
    public void deleteFriendshipRequest(){
        try{
            FriendshipRequest friendshipRequest = tableViewFriendshipRequests_Sent.getSelectionModel().getSelectedItem();
            network.deleteFriendshipRequest(friendshipRequest.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Deleted");
            alert.setContentText("Friendship deleted successfully");
            alert.showAndWait();
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
    }

    /**
     * handles the accept friend request button
     */
    public void approveFriendshipRequest(){
        try{
            FriendshipRequest friendshipRequest = tableViewFriendshipRequests_Received.getSelectionModel().getSelectedItem();
            network.approveFriendshipRequest(friendshipRequest.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Approved");
            alert.setContentText("Friendship approved successfully");
            alert.showAndWait();

        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
        catch (RepositoryException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Already approved/rejected friendship request");
            alert.showAndWait();
        }
    }

    /**
     * handles the reject friend request button
     */
    public void rejectFriendshipRequest(){
        try{
            FriendshipRequest friendshipRequest = tableViewFriendshipRequests_Received.getSelectionModel().getSelectedItem();
            network.rejectFriendshipRequest(friendshipRequest.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Rejected");
            alert.setContentText("Friendship rejected successfully");
            alert.showAndWait();

        }
        catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
        catch (RepositoryException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Already approved/rejected friendship request");
            alert.showAndWait();
        }
    }

    /**
     * Handle chatroomButton action
     */
    public void chatRoomWindow(){ openChatRoomWindow();}

    /**
     * Opens chatroom window
     */
    private void openChatRoomWindow(){
        removeObservers();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chatroomwindow.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        }catch (IOException e){return;}

        ChatRoomController controller = loader.getController();
        controller.setLoggedUser(userId);
        controller.setService(network);

        Stage dialogStage= (Stage) btnMessages.getScene().getWindow();
        dialogStage.setTitle("Chat Rooms");
        Scene scene=new Scene(root);
        dialogStage.setScene(scene);

        dialogStage.show();
    }

    /**
     * handles the show events pane button
     */
    public void showEventsPane(){
        paneEvents.toFront();
    }

    /**
     * handles the add new event button
     */
    public void addEvent(){
        if(datePickerEventDate.getEditor().getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No date selected");
            alert.setContentText("Please select date");
            alert.showAndWait();
            return;
        }

        try{
            String name = textFieldEventName.getText();
            String description = textFieldEventDescription.getText();
            LocalDate date = datePickerEventDate.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(textFieldEventTime.getText(), formatter);
            network.addEvent(name, description, date, time);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Event added successfully");
            alert.showAndWait();
        }
        catch (DateTimeParseException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Time needs to be formatted HH:mm");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        catch (RepositoryException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("There is another identical event");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        catch (ValidatorException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Invalid event");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * handles the subscribe button
     */
    public void addSubscription(){
        try{
            Long user_id = userId;
            Long event_id = tableViewEvents.getSelectionModel().getSelectedItem().getId();
            String event_name = tableViewEvents.getSelectionModel().getSelectedItem().getName();
            String notifications_status = "on";
            network.addSubscription(user_id, event_id, event_name, notifications_status);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Approved");
            alert.setContentText("Subscription added successfully");
            alert.showAndWait();
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an item");
            alert.showAndWait();
        }
        catch (RepositoryException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("Already saved subscription");
            alert.showAndWait();
        }
    }

    /**
     * handles the unsubscribe button
     */
    public void deleteSubscription(){
        try{
            Long subscription_id = tableViewSubscriptions.getSelectionModel().getSelectedItem().getId();
            network.deleteSubscription(subscription_id);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Success");
            alert.setHeaderText("Deleted");
            alert.setContentText("Subscription deleted successfully");
            alert.showAndWait();
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an Event ID");
            alert.showAndWait();
        }
    }

    /**
     * changes the notifications status
     */
    public void switchNotifications(){
        Subscription subscription = tableViewSubscriptions.getSelectionModel().getSelectedItem();
        try{
            network.notificationsSwitch(subscription.getId());
        }
        catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            alert.setTitle("Error");
            alert.setHeaderText("No selected item");
            alert.setContentText("Please select an Event ID");
            alert.showAndWait();
        }
    }

    /**
     * show notifications
     */
    public void showNotifications(){
        AtomicReference<Integer> index = new AtomicReference<>(0);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
            if(!userSubscriptions.isEmpty()) {
                if(userSubscriptions.get(index.get()).getNotifications_status().equals("on")){
                    try{
                        Event event = network.getEventById(userSubscriptions.get(index.get()).getEvent_id());
                        if(event.getDate().compareTo(LocalDate.now()) >= 0) {
                            Long days = ChronoUnit.DAYS.between(LocalDate.now(), event.getDate());
                            String text = "There are " + days + " days left until " + event.getName();
                            lblNotifications.setText(text);
                        }
                    }
                    catch (RepositoryException ex){}
                }
                index.getAndSet(index.get() + 1);
                if(index.get() == userSubscriptions.size())
                    index.set(0);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Handle logout button action
     */
    public void logOut() {
        showMainScene();
    }

    /**
     * Load main scene
     */
    private void showMainScene(){
        showMainWindow();
    }

    /**
     * show the Main window
     */
    private void showMainWindow(){
        removeObservers();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        }catch (IOException e){return;}

        MainController controller = loader.getController();
        controller.setService(network);

        Stage dialogStage= (Stage) btnLogout.getScene().getWindow();
        dialogStage.setTitle("Social Network");
        Scene scene=new Scene(root);
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

    /**
     * update observer
     * @param observerEvent
     */
    @Override
    public void update(ObserverEvent observerEvent) {
        if(observerEvent.getType()== EventType.REMOVE_FRIEND ||
            observerEvent.getType()==EventType.REQUEST_ACCEPTED ||
            observerEvent.getType()==EventType.REQUEST_REJECTED ||
            observerEvent.getType()==EventType.REQUEST_DELETED ||
            observerEvent.getType()==EventType.FRIENDSHIP_ADDED ||
            observerEvent.getType()==EventType.EVENT_ADDED ||
            observerEvent.getType()==EventType.SUBSCRIPTION_ADDED ||
            observerEvent.getType()==EventType.SUBSCRIPTION_DELETED ||
            observerEvent.getType()==EventType.SUBSCRIPTION_NOTIFICATIONS_CHANGED){
            setView();
        }
    }

    /**
     * Unsubscribe from watching services
     */
    private void removeObservers(){
        network.getFriendshipService().removeObservers();
        network.getFriendshipRequestService().removeObservers();
        network.getEventService().removeObservers();
        network.getSubscriptionService().removeObservers();
    }
}
