package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import java.text.ParseException;
import java.util.Date;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class IndexControllerTest extends JavaFxTest {

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

        Contract contract1 = new Contract("A3", new Date(), 2500, getDateFromString("15.08.2030"), "Mobile", "j123", 4150);
        Contract contract2 = new Contract("A4", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380);

        customerService.assignContract(customer, contract1);
        customerService.assignContract(customer, contract2);

        contractService.create(contract1);
        contractService.create(contract2);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/index.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void initTest() {
        verifyThat("#labelTotalExpenses", LabeledMatchers.hasText("2500"));
        verifyThat("#labelTotalNewContracts", LabeledMatchers.hasText("1"));
        verifyThat("#labelTotalIncome", LabeledMatchers.hasText("0"));
        verifyThat("#labelTotalWithdrawnContracts", LabeledMatchers.hasText("0"));
    }

    @Test
    void switchToHome(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#homeButton");

        assertEquals("indexCenterPane", mainPane.getCenter().getId());
    }

    @Test
    void switchToContracts(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#contractsButton");

        assertEquals("contractsHBox", mainPane.getCenter().getId());
    }

    @Test
    void switchToCustomers(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#customersButton");

        assertEquals("customersHBox", mainPane.getCenter().getId());
    }

    @Test
    void switchToOverview(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#overviewButton");

        assertEquals("overviewAnchorPane", mainPane.getCenter().getId());
    }

    @Test
    void switchToAnalytics(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#analyticsButton");

        assertEquals("analyticsHBox", mainPane.getCenter().getId());
    }

    @Test
    void switchToChartAnalytics(final FxRobot robot) {
        BorderPane mainPane = robot.lookup("#mainPane").queryAs(BorderPane.class);
        assertEquals("indexCenterPane", mainPane.getCenter().getId());

        robot.clickOn("#chartButton");

        assertEquals("chartHBox", mainPane.getCenter().getId());
    }

    @Test
    void withdrawContract(final FxRobot robot) {
        TextField contractIdField = robot.lookup("#contractIdField").queryAs(TextField.class);
        contractIdField.setText("A3");

        assertFalse(contractService.getMostRecentByContractId("A3").isWithdrawn());

        robot.clickOn("#withdrawButton");
        robot.clickOn("#notification");

        assertTrue(contractService.getMostRecentByContractId("A3").isWithdrawn());
    }

    @Test
    void takeOutContract(final FxRobot robot) {
        TextField contractIdField = robot.lookup("#contractIdField").queryAs(TextField.class);
        contractIdField.setText("A4");

        assertFalse(contractService.getMostRecentByContractId("A4").isTakenOut());

        robot.clickOn("#takeOutButton");
        robot.clickOn("#notification");

        assertTrue(contractService.getMostRecentByContractId("A4").isTakenOut());
    }

    @Test
    void prolongContract(final FxRobot robot) throws ParseException {
        TextField contractIdField = robot.lookup("#contractIdField").queryAs(TextField.class);
        contractIdField.setText("A4");

        TextField dateField = robot.lookup("#dateField").queryAs(TextField.class);
        dateField.setText("10.10.2022");

        assertEquals(getDateFromString("20.09.2022"), contractService.getMostRecentByContractId("A4").getExpireDateCurr());

        robot.clickOn("#prolongButton");
        robot.clickOn("#notification");

        assertEquals(getDateFromString("10.10.2022"), contractService.getMostRecentByContractId("A4").getExpireDateCurr());
    }
}