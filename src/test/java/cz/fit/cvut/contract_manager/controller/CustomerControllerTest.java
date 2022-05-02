package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

class CustomerControllerTest extends JavaFxTest {

    private ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        customerService.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {

        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", getDateFromString("12.12.00"));

        customerService.create(customer);

        Contract contract1 = new Contract("A3", getDateFromString("10.06.2022"), 2500, getDateFromString("15.08.2022"), "Mobile", "j123", 4150);
        Contract contract2 = new Contract("A4", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380);
        Contract contract3 = new Contract("A5", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556);

        customerService.assignContract(customer, contract1);
        customerService.assignContract(customer, contract2);
        customerService.assignContract(customer, contract3);

        contractService.create(contract1);
        contractService.create(contract2);
        contractService.create(contract3);

        Customer customer2 = new Customer("Annie", "f", "Brno", "slow 123", "velocity", "123445/12", "123f", "V", "vn", getDateFromString("10.12.00"));

        customerService.create(customer2);

        Contract contract4 = new Contract("A6", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556);

        customerService.assignContract(customer2, contract4);
        contractService.create(contract4);

        customerService.create(new Customer("Kimchi", "f", "Prague", "nitro 123", "velocity", "12343/12", "ff2343", "V", "vn", getDateFromString("01.01.00")));

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/customers.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void shouldListCustomersWhenPageIsInitialized() {
        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "123l123", "fast1", "3", "12.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.00"));
    }

    @Test
    void shouldSwitchToCreateCustomerWhenCreateButtonIsPressed(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        robot.clickOn("#createButton");

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void shouldDeleteSelectedCustomerWhenDeleteButtonIsPressed(final FxRobot robot) {
        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.clickOn(node);
        robot.clickOn("#deleteButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_ok"));
        robot.clickOn("#notification");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(2));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.00"));
    }

    @Test
    void shouldSwitchToCreateCustomerWithPreFilledInformationWhenClickingOnCustomer(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());

        verifyThat("#nameField", TextInputControlMatchers.hasText("Mike"));
        verifyThat("#genderField", TextInputControlMatchers.hasText("m"));
        verifyThat("#birthPlaceField", TextInputControlMatchers.hasText("Prague"));
        verifyThat("#addressField", TextInputControlMatchers.hasText("fast1"));
        verifyThat("#cityField", TextInputControlMatchers.hasText("velocity"));
        verifyThat("#personalNumberField", TextInputControlMatchers.hasText("123l123"));
        verifyThat("#cardIdNumberField", TextInputControlMatchers.hasText("a24234"));
        verifyThat("#meuField", TextInputControlMatchers.hasText("V"));
        verifyThat("#nationalityField", TextInputControlMatchers.hasText("vn"));
    }

    @Test
    void shouldFindCustomerByName(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("Mike");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "123l123", "fast1", "3", "12.12.00"));
    }

    @Test
    void shouldFindCustomerByPersonalNumber(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("123l123");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "123l123", "fast1", "3", "12.12.00"));
    }

    @Test
    void shouldUpdateCustomer(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());

        TextField personalNumberField = robot.lookup("#personalNumberField").queryAs(TextField.class);
        personalNumberField.setText("111111/1111");

        robot.clickOn("#updateButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_ok"));
        robot.clickOn("#notification");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "111111/1111", "fast1", "3", "12.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.00"));
    }

    @Test
    void shouldSwitchToCreateContractWithPreFilledCustomerInfoWhenCreateContractButtonIsPressed(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());

        robot.clickOn("#createContractButton");

        verifyThat("#nameField", TextInputControlMatchers.hasText("Mike"));
        verifyThat("#genderField", TextInputControlMatchers.hasText("m"));
        verifyThat("#birthPlaceField", TextInputControlMatchers.hasText("Prague"));
        verifyThat("#addressField", TextInputControlMatchers.hasText("fast1"));
        verifyThat("#cityField", TextInputControlMatchers.hasText("velocity"));
        verifyThat("#personalNumberField", TextInputControlMatchers.hasText("123l123"));
        verifyThat("#cardIdNumberField", TextInputControlMatchers.hasText("a24234"));
        verifyThat("#meuField", TextInputControlMatchers.hasText("V"));
        verifyThat("#nationalityField", TextInputControlMatchers.hasText("vn"));
    }

    @Test
    void shouldNotDeleteCustomerWhenCustomerIsNotSelected(final FxRobot robot) {
        robot.clickOn("#deleteButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_err"));
        robot.clickOn("#notification");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "123l123", "fast1", "3", "12.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.00"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.00"));
    }

    @Test
    void shouldNotFindAnyCustomersWhenGivenWrongInformation(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("NoCustomer");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(0));
    }
}