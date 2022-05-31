package controller;

import domain.Administrator;
import domain.Client;
import domain.Loc;
import domain.Spectacol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.Service;
import utils.IObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdministratorController implements IObserver {
    public Service service;
    public Administrator administrator;
    public Stage stage;
    public FlowPane fp_buttons;
    public List<ToggleButton> selectedSeats = new ArrayList<>();

    public TextField tf_idLoc;
    public TextField tf_pretLoc;
    public TextField tf_zonaLoc;
    public TextField tf_stareLoc;
    public TextField tf_usernameClient;
    public TextField tf_parolaClient;
    public TextField tf_numeClient;
    public TextField tf_cnpClient;
    public TextField tf_telefonClient;

    ObservableList<Client> clienti = FXCollections.observableArrayList();

    public TableColumn<Client, String> tc_username;
    public TableColumn<Client, String> tc_parola;
    public TableColumn<Client, String> tc_nume;
    public TableColumn<Client, String> tc_cnp;
    public TableColumn<Client, String> tc_telefon;
    public TableView<Client> tv_clienti;

    @FXML private Button btn_logout;

    public void set(Service service, Administrator administrator, Stage stage) {
        this.service = service;
        this.administrator = administrator;
        this.stage = stage;
        initButtons();
        setView();
    }

    public void initialize(){
        tc_username.setCellValueFactory(new PropertyValueFactory<Client, String>("username"));
        tc_parola.setCellValueFactory(new PropertyValueFactory<Client, String>("parola"));
        tc_nume.setCellValueFactory(new PropertyValueFactory<Client, String>("nume"));
        tc_cnp.setCellValueFactory(new PropertyValueFactory<Client, String>("cnp"));
        tc_telefon.setCellValueFactory(new PropertyValueFactory<Client, String>("nrTelefon"));
        tv_clienti.setItems(clienti);
    }

    private void initButtons(){
        List<ToggleButton> butoaneLocuri = new ArrayList<>();

        for (Loc loc : service.findAllLocuri()){
            ToggleButton toggleButton = new ToggleButton(loc.getId().toString());
            toggleButton.setFont(Font.font("Monospaced"));
            toggleButton.setMinWidth(40);

            String style = "";

            if (loc.getStare().equals("liber")) {
                style += "-fx-background-color: #12ff00;";
            }
            if (loc.getStare().equals("ocupat")) {
                style += "-fx-background-color: #ff2222;";
            }
            if (loc.getStare().equals("indisponibil")) {
                style += "-fx-background-color: #626161;";
                toggleButton.setDisable(false);
            }

            if (loc.getZona().equals("tribuna")) {
                style += "-fx-text-fill: black;";
            }
            if (loc.getZona().equals("loja")) {
                style += "-fx-text-fill: white;";
            }

            toggleButton.setStyle(style);

            ToggleGroup group = new ToggleGroup();
            toggleButton.setToggleGroup(group);
            toggleButton.setOnAction(e -> {
                if(toggleButton.isSelected()) {
                    selectedSeats.add(toggleButton);
                    tf_idLoc.setText(loc.getId().toString());
                    tf_pretLoc.setText(loc.getPret().toString());
                    tf_zonaLoc.setText(loc.getZona());
                    tf_stareLoc.setText(loc.getStare());
                }
            });

            butoaneLocuri.add(toggleButton);
        }
        fp_buttons.setHgap(5);
        fp_buttons.setVgap(5);
        fp_buttons.getChildren().clear();
        fp_buttons.getChildren().addAll(butoaneLocuri);
    }

    private void setView(){
        try {
            clienti.setAll(service.findAllClienti());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Loc loc) {
        initButtons();
    }

    public void handleLogoutButton(){
        try {
            service.logoutAdministrator(administrator);
            this.goToLogin();
        }
        catch (Exception ex) {
            System.out.println("Could not logout");
        }
    }

    public void handleAdaugareLocButton(){
        Integer pret = Integer.parseInt(tf_pretLoc.getText());
        String zona = tf_zonaLoc.getText();
        String stare = tf_stareLoc.getText();
        Loc loc = new Loc(pret, zona, stare);
        service.addLoc(loc);
        initButtons();
    }

    public void handleActualizareLocButton(){
        Long id = Long.parseLong(tf_idLoc.getText());
        Integer pret = Integer.parseInt(tf_pretLoc.getText());
        String zona = tf_zonaLoc.getText();
        String stare = tf_stareLoc.getText();
        Loc loc = new Loc(id, pret, zona, stare);
        service.updateLoc(loc);
        initButtons();
    }

    public void handleStergereLocButton(){
        Long id = Long.parseLong(tf_idLoc.getText());
        Integer pret = Integer.parseInt(tf_pretLoc.getText());
        String zona = tf_zonaLoc.getText();
        String stare = tf_stareLoc.getText();
        Loc loc = new Loc(id, pret, zona, stare);
        service.deleteLoc(loc);
        initButtons();
    }

    public void handleAdaugareClientButton(){
        String username = tf_usernameClient.getText();
        String parola = tf_parolaClient.getText();
        String nume = tf_numeClient.getText();
        String cnp = tf_cnpClient.getText();
        String telefon = tf_telefonClient.getText();
        Client client = new Client(username, parola, nume, cnp, telefon);
        service.addClient(client);
        setView();
    }

    public void handleActualizareClientButton(){
        Long id = tv_clienti.getSelectionModel().getSelectedItem().getId();
        String username = tf_usernameClient.getText();
        String parola = tf_parolaClient.getText();
        String nume = tf_numeClient.getText();
        String cnp = tf_cnpClient.getText();
        String telefon = tf_telefonClient.getText();
        Client client = new Client(id, username, parola, nume, cnp, telefon);
        service.updateClient(client);
        setView();
    }

    public void handleStergereClientButton(){
        service.deleteClient(tv_clienti.getSelectionModel().getSelectedItem());
        setView();
    }

    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/login-view.fxml"));

            AnchorPane root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login Page");
            loginStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            loginStage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.set(service, loginStage);

            stage.close();
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
