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

import java.util.Date;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

class ContractControllerTest extends JavaFxTest {

    private ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        customerService.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));

        customerService.create(customer);

        Contract contract1 = new Contract("A3", getDateFromString("10.06.22"), 2500, getDateFromString("15.08.22"), "Mobile", "j123", 4150, customer);
        Contract contract2 = new Contract("A4", getDateFromString("04.09.22"), 5500, getDateFromString("20.09.22"), "Mobile", "j123", 6380, customer);
        Contract contract3 = new Contract("A5", getDateFromString("28.09.22"), 3400, getDateFromString("01.11.22"), "Laptop", "j123", 4556, customer);

        contractService.create(contract1);
        contractService.create(contract2);
        contractService.create(contract3);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/contracts.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void shouldListContractsWhenPageIsInitialized() {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));
    }

    @Test
    void shouldSwitchToCreateContractWhenCreateButtonIsPressed(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        robot.clickOn("#createButton");

        assertEquals("createContractAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void shouldSwitchToOverviewWhenOverviewButtonIsPressed(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        robot.clickOn("#overviewButton");

        assertEquals("overviewAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void shouldSwitchToCreateContractWithPreFilledInformation(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createContractAnchorPane", mainPane.getCenter().getId());

        verifyThat("#nameField", TextInputControlMatchers.hasText("Mike"));
        verifyThat("#genderField", TextInputControlMatchers.hasText("m"));
        verifyThat("#birthPlaceField", TextInputControlMatchers.hasText("Prague"));
        verifyThat("#addressField", TextInputControlMatchers.hasText("fast1"));
        verifyThat("#cityField", TextInputControlMatchers.hasText("velocity"));
        verifyThat("#personalNumberField", TextInputControlMatchers.hasText("123l123"));
        verifyThat("#cardIdNumberField", TextInputControlMatchers.hasText("a24234"));
        verifyThat("#meuField", TextInputControlMatchers.hasText("V"));
        verifyThat("#nationalityField", TextInputControlMatchers.hasText("vn"));

        verifyThat("#lendPriceField", TextInputControlMatchers.hasText("2500"));
        verifyThat("#expireDateField", TextInputControlMatchers.hasText("15.08.22"));
        verifyThat("#contractIdField", TextInputControlMatchers.hasText("A3"));
        verifyThat("#itemInfoField", TextInputControlMatchers.hasText("Mobile"));
        verifyThat("#itemSpecificationField", TextInputControlMatchers.hasText("j123"));
        verifyThat("#totalPriceField", TextInputControlMatchers.hasText("4150"));
    }

    @Test
    void shouldFindContractByContractId(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("A3");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
    }

    @Test
    void shouldFindContractsByCustomerName(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("Michal");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(0));

        searchBar.setText("Mike");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));
    }

    @Test
    void shouldFindContractByCreationDate(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("10.06.22");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
    }

    @Test
    void shouldFindContractByItemInformation(final FxRobot robot) {
        TextField searchBar = robot.lookup("#searchBar").queryAs(TextField.class);
        searchBar.setText("Laptop");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));
    }

    @Test
    void shouldDeleteContractWhenSelectedContractIsDeleted(final FxRobot robot) {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.clickOn(node);
        robot.clickOn("#deleteButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_ok"));
        robot.clickOn("#notification");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(2));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));
    }

    @Test
    void shouldNotDeleteContractWhenNoContractIsSelected(final FxRobot robot) {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));

        robot.clickOn("#deleteButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_err"));
        robot.clickOn("#notification");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.22", "15.08.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.22", "20.09.22", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Laptop", 3400, "28.09.22", "01.11.22", null));
    }
}