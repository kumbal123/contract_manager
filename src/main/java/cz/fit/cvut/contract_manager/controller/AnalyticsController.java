package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.ContractState;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AnalyticsController extends Controller {
    public TextField fromField;
    public TextField toField;

    public TableView<Contract> tvContracts;
    public TableColumn<Contract, String> colContractId;
    public TableColumn<Contract, String> colCreationDate;
    public TableColumn<Contract, String> colExpireDate;
    public TableColumn<Contract, String> colLendPrice;
    public TableColumn<Contract, String> colInterest;
    public TableColumn<Contract, String> colTotalPrice;

    public Label labelTotalContracts;
    public Label labelExpenses;
    public Label labelIncome;
    public Label labelProfitLoss;

    public BorderPane mainPane;

    private final ContractRepositoryService contractRepositoryService = ContractRepositoryService.getInstance();

    @FXML
    public void analyzeContracts(final MouseEvent event) throws ParseException {
        Date fromDate = getDateFromString(fromField.getText().trim());
        Date toDate = getDateFromString(toField.getText().trim());

        if(!"".equals(fromDate.toString()) && !"".equals(toDate.toString())) {
            List<Contract> contractList = contractRepositoryService.getAll();

            contractList.removeIf(contract -> contract.getCreationDate().compareTo(fromDate) < 0 || contract.getCreationDate().compareTo(toDate) > 0);

            colContractId.setCellValueFactory(new PropertyValueFactory<>("contractId"));
            colLendPrice.setCellValueFactory(new PropertyValueFactory<>("lendPrice"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPriceCurr"));

            colInterest.setCellValueFactory(cellDate -> {
                int interest = cellDate.getValue().getTotalPriceCurr() - cellDate.getValue().getLendPrice();
                return new SimpleStringProperty(Integer.toString(interest));
            });

            colCreationDate.setCellValueFactory(
                cellData -> getStringPropertyFromDate(cellData.getValue().getCreationDate())
            );

            colExpireDate.setCellValueFactory(
                cellData -> getStringPropertyFromDate(cellData.getValue().getExpireDateCurr())
            );

            tvContracts.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Contract contract, boolean empty) {
                    super.updateItem(contract, empty);

                    if (contract == null || contract.getState() == ContractState.VALID) {
                        setStyle("");
                    } else if (contract.getState() == ContractState.EXPIRED) {
                        setStyle("-fx-background-color: #ffb561;");
                    } else if (contract.getState() == ContractState.WITHDRAWN) {
                        setStyle("-fx-background-color: #64ff61;");
                    } else {
                        setStyle("-fx-background-color: #ff6161;");
                    }
                }
            });

            int expense = 0;
            int income = 0;

            for(Contract contract: contractList) {
                expense += contract.getLendPrice();
                income += contract.getState() == ContractState.WITHDRAWN ? contract.getTotalPriceCurr() : 0;
            }

            labelTotalContracts.setText(Integer.toString(contractList.size()));

            labelExpenses.setText(Integer.toString(expense));
            labelExpenses.setStyle("-fx-background-color: #ff6161;");

            labelIncome.setText(Integer.toString(income));
            labelIncome.setStyle("-fx-background-color: #64ff61;");

            int profitLoss = income - expense;
            labelProfitLoss.setText(Integer.toString(profitLoss));

            if(profitLoss < 0) {
                labelProfitLoss.setStyle("-fx-background-color: #ff6161;");
            } else if(profitLoss > 0) {
                labelProfitLoss.setStyle("-fx-background-color: #64ff61;");
            }

            tvContracts.setItems(FXCollections.observableArrayList(contractList));
        }
    }

    @FXML
    public void handleMouseEvent(final MouseEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
