package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class IndexController extends Controller {

    public TextField contractIdField;
    public TextField dateField;

    private final ContractRepositoryService contractService = ContractRepositoryService.getInstance();

    @FXML
    private BorderPane mainPane;

    @FXML
    private void switchToHome(MouseEvent event) throws IOException {
        Pane pane = (Pane) event.getSource();
        Parent stage = pane.getScene().getRoot();
        stage.getScene().setRoot(getPage("index.fxml"));
    }

    @FXML
    private void switchToContracts(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("contracts.fxml"));
    }

    @FXML
    private void switchToCustomers(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("customers.fxml"));
    }

    @FXML
    private void switchToOverview(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("overview.fxml"));
    }

    @FXML
    public void switchToAnalytics(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("analytics.fxml"));
    }

    @FXML
    private void switchToSettings(final MouseEvent event) throws IOException {
        //TODO
    }

    @FXML
    private void logOut(final MouseEvent event) {
        //TODO
    }

    @FXML
    public void withdrawContract(final MouseEvent event) {
        String contractId = contractIdField.getText().trim();
        Contract contract = contractService.getMostRecentByContractId(contractId);

        contractService.withdraw(contract);
    }

    @FXML
    public void takeOutContract(final MouseEvent event) {
        String contractId = contractIdField.getText().trim();
        Contract contract = contractService.getMostRecentByContractId(contractId);

        contractService.takeOut(contract);
    }

    @FXML
    public void prolongContract(final MouseEvent event) throws ParseException {
        String contractId = contractIdField.getText().trim();
        Date date = getDateFromString(dateField.getText().trim());

        Contract contract = contractService.getMostRecentByContractId(contractId);

        History history = new History(contract.getTotalPriceCurr(), contract.getExpireDateCurr(), date, contract);

        contractService.prolong(contract, history);

        //TODO successful pop up
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
