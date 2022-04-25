package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
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
import javafx.stage.Stage;

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
    private final CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

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
        if(contract != null) {
            customerService.removeContract(contract.getCustomer(), contract);
            contractService.deleteByEntity(contract);
            showContracts();
            Notification.showPopupMessageOk("Hop dong voi so: " + contract.getContractId() + " da xoa xong!", (Stage) mainPane.getScene().getWindow());
            contract = null;
        } else {
            Notification.showPopupMessageErr("Khong xoa duoc. Bam vao hop dong trouc khi xoa!", (Stage) mainPane.getScene().getWindow());
        }
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
                } else if(Contract.getItemInfo().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if(Contract.getItemSpecification().toLowerCase().contains(searchKeyword)) {
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
