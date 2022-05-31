import controller.LoginController;
import domain.Loc;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repository.*;
import service.*;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        HibernateUtil hibernateUtil = new HibernateUtil();

        AdministratorRepository administratorRepository = new AdministratorRepository(hibernateUtil);
        ClientRepository clientRepository = new ClientRepository(hibernateUtil);
        LocRepository locRepository = new LocRepository(hibernateUtil);
        SpectacolRepository spectacolRepository = new SpectacolRepository(hibernateUtil);

        AdministratorService administratorService = new AdministratorService(administratorRepository);
        ClientService clientService = new ClientService(clientRepository);
        LocService locService = new LocService(locRepository);
        SpectacolService spectacolService = new SpectacolService(spectacolRepository);

        Service service = new Service(administratorService, clientService, locService, spectacolService);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login-view.fxml"));
        AnchorPane myPane = loader.load();
        primaryStage.setScene(new Scene(myPane));

        LoginController controller = loader.getController();
        controller.set(service, primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}