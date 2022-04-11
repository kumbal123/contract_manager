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

import java.util.Date;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.*;

import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class ContractControllerTest {

    private ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        customerService.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));

        Contract contract1 = new Contract("A3", getDateFromString("10.06.2022"), 2500, getDateFromString("15.08.2022"), "Mobile", "j123", 4150, customer);
        Contract contract2 = new Contract("A4", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380, customer);
        Contract contract3 = new Contract("A5", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556, customer);

        contractService.create(contract1);
        contractService.create(contract2);
        contractService.create(contract3);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/contracts.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void initializeTest(final FxRobot robot) {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.2022", "15.08.2022", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.2022", "20.09.2022", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Mobile", 3400, "28.09.2022", "01.11.2022", null));
    }

    @Test
    void switchToCreateContractTest(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        robot.clickOn("#createButton");

        assertEquals("createContractAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void switchToOverviewTest(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        robot.clickOn("#overviewButton");

        assertEquals("overviewAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void handleMouseEventTest(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("contractsPane", mainPane.getCenter().getId());

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.doubleClickOn(node);

        assertEquals("createContractAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void deleteContractTest(final FxRobot robot) {
        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A3", "Mobile", 2500, "10.06.2022", "15.08.2022", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.2022", "20.09.2022", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Mobile", 3400, "28.09.2022", "01.11.2022", null));

        Node node = robot.lookup("#colPersonalNumber").nth(1).query();
        robot.clickOn(node);
        robot.clickOn("#deleteButton");
        robot.clickOn("#notification");

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(2));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A4", "Mobile", 5500, "04.09.2022", "20.09.2022", null));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("123l123", "Mike", "A5", "Mobile", 3400, "28.09.2022", "01.11.2022", null));
    }
}