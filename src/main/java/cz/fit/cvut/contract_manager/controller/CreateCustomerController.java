package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.text.ParseException;
import java.util.ResourceBundle;

public class CreateCustomerController extends Controller {
    public TextField nameField;
    public TextField genderField;
    public TextField addressField;
    public TextField cityField;
    public TextField personalNumberField;
    public TextField cardIdNumberField;
    public TextField meuField;
    public TextField nationalityField;
    public TextField dateOfBirthField;

    public Button updateButton;
    public Button createContractButton;
    public BorderPane mainPane;

    public CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();
    private Customer customer;

    public void initUpdateCustomer(final Customer src) {
        customer = src;

        nameField.setText(customer.getName());
        genderField.setText(customer.getGender());
        addressField.setText(customer.getAddress());
        cityField.setText(customer.getCity());
        personalNumberField.setText(customer.getPersonalNumber());
        cardIdNumberField.setText(customer.getCardIdNumber());
        meuField.setText(customer.getMeu());
        nationalityField.setText(customer.getNationality());
        dateOfBirthField.setText(getStringFromDate(customer.getDateOfBirth()));

        updateButton.setDisable(false);
        createContractButton.setDisable(false);
    }

    @FXML
    public void createCustomer(final MouseEvent event) throws IOException, ParseException {
        String name = nameField.getText().trim();
        String gender = genderField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String personalNumber = personalNumberField.getText().trim();
        String cardIdNumber = cardIdNumberField.getText().trim();
        String meu = meuField.getText().trim();
        String nationality = nationalityField.getText().trim();
        Date dateOfBirth = getDateFromString(dateOfBirthField.getText().trim());

        customer = new Customer(name, gender, address, city, personalNumber, cardIdNumber, meu, nationality, dateOfBirth);

        customerService.create(customer);

        mainPane.setCenter(getPage("customers.fxml"));
    }

    @FXML
    public void updateCustomer(final MouseEvent event) throws IOException, ParseException {
        customer.setName(nameField.getText().trim());
        customer.setGender(genderField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        customer.setCity(cityField.getText().trim());
        customer.setPersonalNumber(personalNumberField.getText().trim());
        customer.setCardIdNumber(cardIdNumberField.getText().trim());
        customer.setMeu(meuField.getText().trim());
        customer.setNationality(nationalityField.getText().trim());
        customer.setDateOfBirth(getDateFromString(dateOfBirthField.getText().trim()));

        customerService.update(customer);

        mainPane.setCenter(getPage("customers.fxml"));
    }

    @FXML
    public void createContract(final MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/createContract.fxml"));
        Pane pane = loader.load();

        CreateContractController createContractController = loader.getController();
        createContractController.initCustomerData(customer);

        mainPane.setCenter(pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
