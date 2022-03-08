package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContractController extends Controller {

    public TableView<Contract> tvContracts;
    public TableColumn<Contract, String> colPersonalNumber;
    public TableColumn<Contract, String> colName;
    public TableColumn<Contract, String> colContractId;
    public TableColumn<Contract, String> colItemInfo;
    public TableColumn<Contract, String> colLendPrice;
    public TableColumn<Contract, String> colCreationDate;
    public TableColumn<Contract, String> colExpireDate;

    public TextField searchBar;
    public BorderPane mainPane;

    private Contract contract;

    private final Integer contractLimitDisplay = 20;
    private final ContractRepositoryService contractService = ContractRepositoryService.getInstance();

    @FXML
    public void switchToCreateContract(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("createContract.fxml"));
    }

    @FXML
    public void switchToOverview(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("overview.fxml"));
    }

    @FXML
    public void deleteContract(final MouseEvent event) {
        contractService.removeCustomer(contract);
        contractService.deleteByEntity(contract);
        showContracts();
    }

    @FXML
    public void handleMouseEvent(final MouseEvent event) throws IOException {
        contract = tvContracts.getSelectionModel().getSelectedItem();

        if(event.getClickCount() > 1 && contract != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/createContract.fxml"));
            Pane pane = loader.load();

            CreateContractController createContractController = loader.getController();
            createContractController.initContractData(contract);
            createContractController.initCustomerData(contract.getCustomer());

            mainPane.setCenter(pane);
        }
    }

    private void showContracts() {
        ObservableList<Contract> contractList = FXCollections.observableArrayList(contractService.getAll());

        colPersonalNumber.setCellValueFactory(new PropertyValueFactory<>("personalNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContractId.setCellValueFactory(new PropertyValueFactory<>("contractId"));
        colItemInfo.setCellValueFactory(new PropertyValueFactory<>("itemInfo"));
        colLendPrice.setCellValueFactory(new PropertyValueFactory<>("lendPrice"));

        colCreationDate.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getCreationDate())
        );

        colExpireDate.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getExpireDateOrig())
        );

        FilteredList<Contract> filteredList =
            new FilteredList<>(contractList, contract -> contractList.indexOf(contract) < contractLimitDisplay);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showContracts();
    }
}
