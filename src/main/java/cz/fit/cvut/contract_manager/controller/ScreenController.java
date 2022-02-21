package cz.fit.cvut.contract_manager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {

    @FXML
    public void switchToHome(MouseEvent event) throws IOException {
        switchScreen(event, "index.fxml");
    }

    @FXML
    public void switchToContracts(MouseEvent event) throws IOException {
        switchScreen(event, "contracts.fxml");
    }

    @FXML
    public void switchToCustomers(MouseEvent event) throws IOException {
        switchScreen(event, "customers.fxml");
    }

    @FXML
    public void switchToOverview(MouseEvent event) throws IOException {
        switchScreen(event, "overview.fxml");
    }

    @FXML
    public void switchToSettings(MouseEvent event) throws IOException {
        switchScreen(event, "settings.fxml");
    }

    @FXML
    public void switchToCreateContract(MouseEvent event) throws IOException {
        switchScreen(event, "createContract.fxml");
    }

    public void switchScreen(final MouseEvent event, final String src) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + src));
        stage.setTitle(src);
        stage.setScene(new Scene(root));
        stage.show();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
