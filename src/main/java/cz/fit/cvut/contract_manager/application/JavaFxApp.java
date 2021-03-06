package cz.fit.cvut.contract_manager.application;

import cz.fit.cvut.contract_manager.service.BootService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxApp extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BootService.bootApp();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/index.fxml"));
        primaryStage.setTitle("Contract Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
