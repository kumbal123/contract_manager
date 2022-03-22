package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Customer;
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

public class CustomerController extends Controller {

    public TableView<Customer> tvCustomers;
    public TableColumn<Customer, String> colName;
    public TableColumn<Customer, String> colPersonalNumber;
    public TableColumn<Customer, String> colAddress;
    public TableColumn<Customer, String> colNumOfContracts;
    public TableColumn<Customer, String> colDateOfBirth;

    public BorderPane mainPane;
    public TextField searchBar;

    private Customer customer;

    private final CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @FXML
    public void switchToCreateCustomer(final MouseEvent event) throws IOException {
        mainPane.setCenter(getPage("createCustomer.fxml"));
    }

    @FXML
    public void deleteCustomer(final MouseEvent event) {
        if(customer != null) {
            customerService.deleteByEntity(customer);
            showCustomers();
            Notification.showPopupMessageOk("Successfully deleted customer: " + customer.getName() + "!", (Stage) mainPane.getScene().getWindow());
            customer = null;
        } else {
            Notification.showPopupMessageErr("Could not delete. Pick a customer by clicking on it first!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void editCustomer(final MouseEvent event) throws IOException {
        customer = tvCustomers.getSelectionModel().getSelectedItem();

        if(event.getClickCount() > 1 && customer != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/createCustomer.fxml"));
            Pane pane = loader.load();

            CreateCustomerController createCustomerController = loader.getController();
            createCustomerController.initUpdateCustomer(customer);

            mainPane.setCenter(pane);
        }
    }

    private void showCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList(customerService.getAll());

        colName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        colPersonalNumber.setCellValueFactory(new PropertyValueFactory<Customer, String>("personalNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        colNumOfContracts.setCellValueFactory(new PropertyValueFactory<Customer, String>("numberOfContracts"));

        colDateOfBirth.setCellValueFactory(
            cellData -> getStringPropertyFromDate(cellData.getValue().getDateOfBirth())
        );

        tvCustomers.setItems(customerList);

        FilteredList<Customer> filteredList = new FilteredList<>(customerList, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(Customer -> {
                if(newValue.isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(Customer.getPersonalNumber().toLowerCase().contains(searchKeyword)) {
                    return true;
                }else {
                    return Customer.getName().toLowerCase().contains(searchKeyword);
                }
            });
        });

        SortedList<Customer> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tvCustomers.comparatorProperty());
        tvCustomers.setItems(sortedList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCustomers();
    }
}
