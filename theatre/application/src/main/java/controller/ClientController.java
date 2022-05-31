package controller;

import domain.Client;
import domain.Loc;
import domain.Spectacol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;
import utils.IObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientController implements IObserver {
    public Service service;
    public Client client;
    public Stage stage;
    public FlowPane fp_buttons;
    public Button btn_logout;
    public Button btn_informatii;
    public List<ToggleButton> selectedSeats = new ArrayList<>();

    public void set(Service service, Client client, Stage stage) {
        this.service = service;
        this.client = client;
        this.stage = stage;
        initButtons();
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
                toggleButton.setDisable(true);
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
                }
            });

            butoaneLocuri.add(toggleButton);
        }
        fp_buttons.setHgap(5);
        fp_buttons.setVgap(5);
        fp_buttons.getChildren().clear();
        fp_buttons.getChildren().addAll(butoaneLocuri);
    }

    @Override
    public void update(Loc loc) {
        initButtons();
    }

    public void handleLogoutButton(){
        try {
            service.logoutClient(client);
            this.goToLogin();
        }
        catch (Exception ex) {
            System.out.println("Could not logout");
        }
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

    public void handleRezervareButton() {
        if(selectedSeats.isEmpty()){
            errorMessageSelectare();
        }

        for(ToggleButton toggleButton : selectedSeats){
            Long idLoc = Long.valueOf(toggleButton.getText());
            Loc locN = service.findLoc(idLoc);
            locN.setStare("ocupat");
            service.updateLoc(locN);
        }
        selectedSeats.clear();
        initButtons();
    }

    public void handleAnulareButton() {
        if(selectedSeats.isEmpty()){
            errorMessageSelectare();
        }

        for(ToggleButton toggleButton : selectedSeats){
            Long idLoc = Long.valueOf(toggleButton.getText());
            Loc locN = service.findLoc(idLoc);
            locN.setStare("liber");
            service.updateLoc(locN);
        }
        selectedSeats.clear();
        initButtons();
    }

    public void handleInformatiiButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/views/style.css")).toExternalForm());
        dialogPane.setGraphic(null);
        alert.setTitle("Spectacolul de azi");
        alert.setHeaderText("Informatii spectacol");
        Spectacol spectacol = service.findSpectacol(1L);
        String content = "\nNume:\n" + spectacol.getNume() + "\n\nDescriere:\n" + spectacol.getDescriere();
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleInstructiuniButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/views/style.css")).toExternalForm());
        dialogPane.setGraphic(null);
        alert.setTitle("Instructiuni");
        alert.setHeaderText("Instructiuni");
        String content = """
                Pas 1: Logare
                Utilizatorul completeaza casutele credetialelor contului sau si apasa butonul "Login".
                
                Pas 2: Rezervare locuri
                Utilizatorul selecteaza locurile dorite pentru rezervare si apasa butonul "Rezervare".
                
                Pas 3: Anulare locuri
                Utilizatorul selecteaza locurile dorite pentru anulare si apasa butonul "Anulare".
                
                Pas 4: Delogare
                Utilizatorul se delogheaza din contul sau cu ajutorul butonului "Delogare".
                """;
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void errorMessageSelectare(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/views/style.css")).toExternalForm());
        dialogPane.setGraphic(null);
        alert.setTitle("Eroare selectare");
        alert.setHeaderText("Nu au fost selectate locuri.");
        alert.setContentText("Dati click pe un loc pentru a-l selecta.");
        alert.showAndWait();
    }
}
