package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewController extends Controller {

    public TableView<Contract> tvContracts;
    public TableColumn<Contract, String> colName;
    public TableColumn<Contract, String> colCreationDate;
    public TableColumn<Contract, String> colExpireDateOrig;
    public TableColumn<Contract, String> colExpireDateCurr;
    public TableColumn<Contract, String> colItemInfo;
    public TableColumn<Contract, String> colContractId;
    public TableColumn<Contract, String> colLendPrice;
    public TableColumn<Contract, String> colTotalPrice;
    public TableColumn<Contract, String> colDiff;

    public TextField searchBar;
    public BorderPane mainPane;

    private final ContractRepositoryService contractService = ContractRepositoryService.getInstance();

    public void showContracts() {
        ObservableList<Contract> contractList = FXCollections.observableArrayList(contractService.getAll());

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemInfo.setCellValueFactory(new PropertyValueFactory<>("itemInfo"));
        colContractId.setCellValueFactory(new PropertyValueFactory<>("contractId"));
        colLendPrice.setCellValueFactory(new PropertyValueFactory<>("lendPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPriceOrig"));

        colCreationDate.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getCreationDate())
        );

        colExpireDateOrig.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getExpireDateOrig())
        );

        colExpireDateCurr.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getExpireDateCurr())
        );

        colDiff.setCellValueFactory(cellData -> {
            int diff = cellData.getValue().getTotalPriceOrig() - cellData.getValue().getLendPrice();
            return new SimpleStringProperty(Integer.toString(diff));
        });

        tvContracts.setRowFactory(tv -> new TableRow<Contract>() {
            @Override
            protected void updateItem(Contract contract, boolean empty) {
            super.updateItem(contract, empty);

            if(contract == null || contract.isValid()) {
                setStyle("");
            } else if(contract.isExpired()) {
                setStyle("-fx-background-color: #ffb561;");
            } else if(contract.isWithdrawn()) {
                setStyle("-fx-background-color: #64ff61;");
            } else {
                setStyle("-fx-background-color: #ff6161;");
            }
            }
        });

        FilteredList<Contract> filteredList = new FilteredList<>(contractList, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(Contract -> {
                if(newValue.isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(Contract.getPersonalNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if(Contract.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if(getStringFromDate(Contract.getCreationDate()).contains(searchKeyword)) {
                    return true;
                } else {
                    return Contract.getContractId().toLowerCase().contains(searchKeyword);
                }
            });
        });

        SortedList<Contract> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tvContracts.comparatorProperty());
        tvContracts.setItems(sortedList);
    }

    @FXML
    public void handleMouseEvent(final MouseEvent event) throws IOException {
        Contract contract = tvContracts.getSelectionModel().getSelectedItem();

        if(event.getClickCount() > 1 && contract != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/viewContract.fxml"));
            Pane pane = loader.load();

            ViewContractController viewContractController = loader.getController();
            viewContractController.initContractData(contract);

            mainPane.setCenter(pane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showContracts();
    }
}
