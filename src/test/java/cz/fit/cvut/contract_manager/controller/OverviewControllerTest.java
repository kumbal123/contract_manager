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
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TableViewMatchers;

import java.util.Date;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

class OverviewControllerTest extends JavaFxTest {

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

        Contract contract1 = new Contract("A3", getDateFromString("10.07.22"), 2500, getDateFromString("15.08.22"), "Mobile", "j123", 4150, customer);
        Contract contract2 = new Contract("A4", getDateFromString("04.09.22"), 5500, getDateFromString("20.09.22"), "Mobile", "j123", 6380, customer);

        contractService.create(contract1);
        contractService.create(contract2);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/overview.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void showContracts() {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(2));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("Mike", "10.07.22", "15.08.22", "15.08.22", "Mobile", "A3", 2500, 4150, "1650"));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("Mike", "04.09.22", "20.09.22", "20.09.22", "Mobile", "A4", 5500, 6380, "880"));
    }

    @Test
    void handleMouseEvent(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("overviewPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colName").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("viewContractAnchorPane", mainPane.getCenter().getId());
    }
}