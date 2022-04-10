package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.HistoryRepositoryService;
import cz.fit.cvut.contract_manager.util.Util;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

public class IndexController extends Controller {

    public TextField contractIdField;
    public TextField dateField;

    public Label labelTotalExpenses;
    public Label labelTotalNewContracts;
    public Label labelTotalExpenses0;
    public Label labelTotalNewContracts0;
    public Label labelTotalIncome;
    public Label labelTotalWithdrawnContracts;

    private final ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private final HistoryRepositoryService historyService = HistoryRepositoryService.getInstance();

    @FXML
    private BorderPane mainPane;

    @FXML
    private void switchToHome(final MouseEvent event) throws IOException {
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
    public void switchToChartAnalytics(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("chartAnalytics.fxml"));
    }

    @FXML
    private void switchToSettings(final MouseEvent event) throws IOException {
        //TODO
    }

    @FXML
    public void withdrawContract(final MouseEvent event) {
        String contractId = contractIdField.getText().trim();
        Contract contract = contractService.getMostRecentByContractId(contractId);

        if(contract != null && contractService.withdraw(contract)) {
            Notification.showPopupMessageOk("Withdraw was successful!", (Stage) mainPane.getScene().getWindow());
        } else if(contract != null) {
            Notification.showPopupMessageErr(
                "Contract is already " + (contract.isWithdrawn() ? "withdrawn" : "taken out"),
                (Stage) mainPane.getScene().getWindow()
            );
        } else {
            Notification.showPopupMessageErr(
                "Contract with " + contractId + " id does not exist!",
                (Stage) mainPane.getScene().getWindow()
            );
        }
    }

    @FXML
    public void takeOutContract(final MouseEvent event) {
        String contractId = contractIdField.getText().trim();
        Contract contract = contractService.getMostRecentByContractId(contractId);

        if(contract != null && contractService.takeOut(contract)) {
            Notification.showPopupMessageOk("Takeout was successful!", (Stage) mainPane.getScene().getWindow());
        } else if(contract != null) {
            Notification.showPopupMessageErr(
                "Contract is already " + (contract.isWithdrawn() ? "withdrawn" : "taken out"),
                (Stage) mainPane.getScene().getWindow()
            );
        } else {
            Notification.showPopupMessageErr(
                "Contract with " + contractId + " id does not exist!",
                (Stage) mainPane.getScene().getWindow()
            );
        }
    }

    @FXML
    public void prolongContract(final MouseEvent event) throws ParseException {
        String contractId = contractIdField.getText().trim();
        String dateStr = dateField.getText().trim();

        if(isDate(dateStr)) {
            Contract contract = contractService.getMostRecentByContractId(contractId);

            if(contract == null) {
                Notification.showPopupMessageErr(
                    "Contract with " + contractId + " id does not exist!",
                    (Stage) mainPane.getScene().getWindow()
                );
            } else {
                History history = new History(contract.getTotalPriceCurr(), contract.getExpireDateCurr(),
                        getDateFromString(dateStr), contract);

                if(contractService.prolong(contract, history)) {
                    historyService.create(history);
                    Notification.showPopupMessageOk("Prolong was successful!", (Stage) mainPane.getScene().getWindow());
                } else {
                    Notification.showPopupMessageErr(
                        "Contract is already " + (contract.isWithdrawn() ? "withdrawn" : "taken out"),
                        (Stage) mainPane.getScene().getWindow()
                    );
                }
            }
        } else {
            Notification.showPopupMessageErr(
                "Date is not actually date!",
                (Stage) mainPane.getScene().getWindow()
            );
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Contract> contractList = contractService.getAll();

        int totalExpenses0 = 0, totalNewContracts0 = 0;
        int totalExpenses = 0, totalNewContracts = 0, totalIncome = 0, totalWithdrawnContracts = 0;

        for(Contract contract : contractList) {
            if(Util.isToday(contract.getCreationDate())) {
                if(contract.getContractId().charAt(1) != '0') {
                    totalExpenses += contract.getLendPrice();
                    totalNewContracts += 1;
                } else {
                    totalExpenses0 += contract.getLendPrice();
                    totalNewContracts0 += 1;
                }
            } else if(Util.isToday(contract.getExpireDateCurr()) && contract.isWithdrawn()) {
                totalIncome += contract.getTotalPriceCurr();
                totalWithdrawnContracts += 1;
            }
        }

        labelTotalExpenses.setText(String.valueOf(totalExpenses));
        labelTotalNewContracts.setText(String.valueOf(totalNewContracts));
        labelTotalExpenses0.setText(String.valueOf(totalExpenses0));
        labelTotalNewContracts0.setText(String.valueOf(totalNewContracts0));
        labelTotalIncome.setText(String.valueOf(totalIncome));
        labelTotalWithdrawnContracts.setText(String.valueOf(totalWithdrawnContracts));
    }
}
