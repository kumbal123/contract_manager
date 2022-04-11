package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TableViewMatchers;

import java.text.ParseException;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class AnalyticsControllerTest {

    private ContractRepositoryService service = ContractRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        service.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/analytics.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void shouldAnalyzeContracts(final FxRobot robot) throws ParseException {
        service.create(new Contract("A1", getDateFromString("01.02.2022"), 1000, getDateFromString("01.03.2022"), "Mobile", "j123", 1280, null));
        service.create(new Contract("A3", getDateFromString("20.04.2022"), 1500, getDateFromString("10.06.2022"), "Mobile", "j123", 2265, null));

        service.create(new Contract("A2", getDateFromString("10.06.2022"), 2500, getDateFromString("15.08.2022"), "Mobile", "j123", 4150, null));
        service.create(new Contract("A4", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556, null));
        service.create(new Contract("A5", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380, null));

        int totalContracts = 3, totalExpenses = 2500 + 3400 + 5500, totalIncome = 0, profitLoss = -1 * totalExpenses;

        TextField fromField = robot.lookup("#fromField").queryAs(TextField.class);
        fromField.setText("01.05.2022");

        TextField toField = robot.lookup("#toField").queryAs(TextField.class);
        toField.setText("01.10.2022");

        robot.clickOn("#analyzeButton");

        verifyThat("#labelTotalContracts", LabeledMatchers.hasText(String.valueOf(totalContracts)));
        verifyThat("#labelExpenses", LabeledMatchers.hasText(String.valueOf(totalExpenses)));
        verifyThat("#labelIncome", LabeledMatchers.hasText(String.valueOf(totalIncome)));
        verifyThat("#labelProfitLoss", LabeledMatchers.hasText(String.valueOf(profitLoss)));

        verifyThat("#tvContracts", TableViewMatchers.hasNumRows(3));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("A2", "10.06.2022", "15.08.2022", 2500, "1650", 4150));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("A4", "28.09.2022", "01.11.2022", 3400, "1156", 4556));
        verifyThat("#tvContracts", TableViewMatchers.containsRow("A5", "04.09.2022", "20.09.2022", 5500, "880", 6380));
    }
}