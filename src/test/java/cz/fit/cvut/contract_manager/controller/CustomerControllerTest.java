package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TableViewMatchers;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class CustomerControllerTest {

    private ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        customerService.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {

        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", getDateFromString("12.12.2000"));

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

        Customer customer2 = new Customer("Annie", "f", "Brno", "slow 123", "velocity", "123445/12", "123f", "V", "vn", getDateFromString("10.12.2000"));

        customerService.create(customer2);

        Contract contract4 = new Contract("A6", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556);

        customerService.assignContract(customer2, contract4);
        contractService.create(contract4);

        customerService.create(new Customer("Kimchi", "f", "Prague", "nitro 123", "velocity", "12343/12", "ff2343", "V", "vn", getDateFromString("01.01.2000")));

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/customers.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void switchToCreateCustomer(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        robot.clickOn("#createButton");

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void deleteCustomer(final FxRobot robot) {
        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Mike", "123l123", "fast1", "3", "12.12.2000"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.2000"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.2000"));

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.clickOn(node);
        robot.clickOn("#deleteButton");
        robot.clickOn("#notification");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(2));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Annie", "123445/12", "slow 123", "1", "10.12.2000"));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow("Kimchi", "12343/12", "nitro 123", "0", "01.01.2000"));
    }

    @Test
    void shouldSwitchToCreateCustomerPaneWhenClickingOnCustomerTest(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("customersPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createCustomerAnchorPane", mainPane.getCenter().getId());
    }
}