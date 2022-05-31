package controller;

import domain.Administrator;
import domain.Client;
import domain.Spectacol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.Service;
import service.ServiceException;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML private TextField tf_username;
    @FXML private TextField tf_parola;
    @FXML private Button btn_login;
    @FXML private CheckBox cb_admin;

    private Service service;
    private Stage stage;

    public void set(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    @FXML
    public void handleLoginButton() throws ServiceException {
        String username = tf_username.getText();
        String parola = tf_parola.getText();

        try {
            if (cb_admin.isSelected()){
                Administrator administrator = service.loginAdministrator(username, parola);
                if (administrator != null) {
                    showWindowAdministrator(administrator);
                }
            }
            else {
                Client client = service.loginClient(username, parola);
                if (client != null) {
                    showWindowClient(client);
                }
            }
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/views/style.css")).toExternalForm());
            dialogPane.setGraphic(null);
            alert.setTitle("Eroare autentificare");
            alert.setHeaderText("Credentiale invalide.");
            alert.setContentText("Va rugam sa reintroduceti datele.");
            alert.showAndWait();
            tf_username.clear();
            tf_parola.clear();
        }
    }

    private void showWindowAdministrator(Administrator administrator){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/administrator-view.fxml"));

            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Administrator: " + administrator.getNume());
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            AdministratorController administratorController = loader.getController();
            service.setObserverAdministrator(administrator.getId(), administratorController);
            administratorController.set(service, administrator, stage);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWindowClient(Client client){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/client-view.fxml"));

            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Client: " + client.getNume());
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            ClientController clientController = loader.getController();
            service.setObserverClient(client.getId(), clientController);
            clientController.set(service, client, stage);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
