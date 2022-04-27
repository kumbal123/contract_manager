package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import static cz.fit.cvut.contract_manager.controller.Controller.getDateFromString;
import static org.testfx.api.FxAssert.verifyThat;

class ChartAnalyticsControllerTest extends JavaFxTest {

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

        contractService.create(new Contract("A1", getDateFromString("20.04.2019"), 1500, getDateFromString("10.06.2022"), "Mobile", "j123", 2265, customer));

        contractService.create(new Contract("A2", getDateFromString("01.02.2020"), 1000, getDateFromString("01.03.2022"), "Mobile", "j123", 1280, customer));

        Contract contract3 = new Contract("A3", getDateFromString("10.06.2022"), 2500, getDateFromString("15.08.2022"), "Mobile", "j123", 4150, customer);
        contractService.create(contract3);
        contractService.takeOut(contract3);

        contractService.create(new Contract("A4", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380, customer));
        contractService.create(new Contract("A5", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556, customer));

        contractService.create(new Contract("A10", getDateFromString("10.06.2023"), 2500, getDateFromString("15.08.2023"), "Mobile", "j123", 4150, customer));
        contractService.create(new Contract("A21", getDateFromString("04.09.2023"), 5500, getDateFromString("20.09.2023"), "Mobile", "j123", 6380, customer));
        contractService.create(new Contract("A33", getDateFromString("28.09.2023"), 3400, getDateFromString("01.11.2023"), "Mobile", "j123", 4556, customer));

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/chartAnalytics.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void clearCharts(final FxRobot robot) {
        robot.clickOn("#clearButton");

        verifyThat("#labelTotalContracts", LabeledMatchers.hasText("0"));
        verifyThat("#labelTotalExpenses", LabeledMatchers.hasText("0"));
        verifyThat("#labelTotalIncome", LabeledMatchers.hasText("0"));
        verifyThat("#labelTotalProfitLoss", LabeledMatchers.hasText("0"));

        ObservableList<PieChart.Data> pieChartData = robot.lookup("#pieChart").queryAs(PieChart.class).getData();

        Assert.assertTrue(pieChartData.isEmpty());

        LineChart<String, Integer> lineChartIncome = robot.lookup("#lineChartIncome").query();

        Assert.assertTrue(lineChartIncome.getData().isEmpty());

        LineChart<String, Integer> lineChartExpenses = robot.lookup("#lineChartExpenses").query();

        Assert.assertTrue(lineChartExpenses.getData().isEmpty());

        LineChart<String, Integer> lineChartProfitLoss = robot.lookup("#lineChartProfitLoss").query();

        Assert.assertTrue(lineChartProfitLoss.getData().isEmpty());
    }

    @Test
    void analyzeContractsInMonths(final FxRobot robot) {
        int totalContracts = 3, totalExpenses = 2500 + 3400 + 5500, totalIncome = 0, profitLoss = -1 * totalExpenses;
        int totalExpensesJun = 2500, totalExpensesSep = 8900;

        robot.clickOn("#clearButton");

        TextField fromField = robot.lookup("#fromYearField").queryAs(TextField.class);
        fromField.setText("2022");

        robot.clickOn("#monthsButton");

        verifyThat("#labelTotalContracts", LabeledMatchers.hasText(String.valueOf(totalContracts)));
        verifyThat("#labelTotalExpenses", LabeledMatchers.hasText(String.valueOf(totalExpenses)));
        verifyThat("#labelTotalIncome", LabeledMatchers.hasText(String.valueOf(totalIncome)));
        verifyThat("#labelTotalProfitLoss", LabeledMatchers.hasText(String.valueOf(profitLoss)));

        ObservableList<PieChart.Data> pieChartData = robot.lookup("#pieChart").queryAs(PieChart.class).getData();

        double delta = 0.01;

        Assert.assertEquals(1, pieChartData.get(0).getPieValue(), delta);
        Assert.assertEquals(0, pieChartData.get(1).getPieValue(), delta);
        Assert.assertEquals(2, pieChartData.get(2).getPieValue(), delta);

        LineChart<String, Integer> lineChartIncome = robot.lookup("#lineChartIncome").query();
        ObservableList<XYChart.Data<String, Integer>> incomeData = lineChartIncome.getData().get(0).getData();

        for(XYChart.Data<String, Integer> data : incomeData) {
            Assert.assertEquals(0, data.getYValue(), delta);
        }

        LineChart<String, Integer> lineChartExpenses = robot.lookup("#lineChartExpenses").query();
        ObservableList<XYChart.Data<String, Integer>> expensesData = lineChartExpenses.getData().get(0).getData();

        Assert.assertEquals(0, expensesData.get(0).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(1).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(2).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(3).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(4).getYValue(), delta);
        Assert.assertEquals(totalExpensesJun, expensesData.get(5).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(6).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(7).getYValue(), delta);
        Assert.assertEquals(totalExpensesSep, expensesData.get(8).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(9).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(10).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(11).getYValue(), delta);

        LineChart<String, Integer> lineChartProfitLoss = robot.lookup("#lineChartProfitLoss").query();
        ObservableList<XYChart.Data<String, Integer>> profitLossData = lineChartProfitLoss.getData().get(0).getData();

        Assert.assertEquals(0, profitLossData.get(0).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(1).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(2).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(3).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(4).getYValue(), delta);
        Assert.assertEquals(-totalExpensesJun, profitLossData.get(5).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(6).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(7).getYValue(), delta);
        Assert.assertEquals(-totalExpensesSep, profitLossData.get(8).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(9).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(10).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(11).getYValue(), delta);
    }

    @Test
    void analyzeContractsInYears(final FxRobot robot) {
        int totalExpenses2019 = 1500, totalExpenses2020 = 1000, totalExpenses2022 = 2500 + 3400 + 5500;
        int totalContracts = 5, totalExpenses = totalExpenses2019 + totalExpenses2020 + totalExpenses2022;
        int totalIncome = 0, profitLoss = -1 * totalExpenses;

        robot.clickOn("#clearButton");

        TextField fromField = robot.lookup("#fromYearField").queryAs(TextField.class);
        fromField.setText("2019");

        TextField toField = robot.lookup("#toYearField").queryAs(TextField.class);
        toField.setText("2022");

        robot.clickOn("#yearsButton");

        verifyThat("#labelTotalContracts", LabeledMatchers.hasText(String.valueOf(totalContracts)));
        verifyThat("#labelTotalExpenses", LabeledMatchers.hasText(String.valueOf(totalExpenses)));
        verifyThat("#labelTotalIncome", LabeledMatchers.hasText(String.valueOf(totalIncome)));
        verifyThat("#labelTotalProfitLoss", LabeledMatchers.hasText(String.valueOf(profitLoss)));

        ObservableList<PieChart.Data> pieChartData = robot.lookup("#pieChart").queryAs(PieChart.class).getData();

        double delta = 0.01;

        Assert.assertEquals(1, pieChartData.get(0).getPieValue(), delta);
        Assert.assertEquals(0, pieChartData.get(1).getPieValue(), delta);
        Assert.assertEquals(4, pieChartData.get(2).getPieValue(), delta);

        LineChart<String, Integer> lineChartIncome = robot.lookup("#lineChartIncome").query();
        ObservableList<XYChart.Data<String, Integer>> incomeData = lineChartIncome.getData().get(0).getData();

        for(XYChart.Data<String, Integer> data : incomeData) {
            Assert.assertEquals(0, data.getYValue(), delta);
        }

        LineChart<String, Integer> lineChartExpenses = robot.lookup("#lineChartExpenses").query();
        ObservableList<XYChart.Data<String, Integer>> expensesData = lineChartExpenses.getData().get(0).getData();

        Assert.assertEquals(totalExpenses2019, expensesData.get(0).getYValue(), delta);
        Assert.assertEquals(totalExpenses2020, expensesData.get(1).getYValue(), delta);
        Assert.assertEquals(0, expensesData.get(2).getYValue(), delta);
        Assert.assertEquals(totalExpenses2022, expensesData.get(3).getYValue(), delta);

        LineChart<String, Integer> lineChartProfitLoss = robot.lookup("#lineChartProfitLoss").query();
        ObservableList<XYChart.Data<String, Integer>> profitLossData = lineChartProfitLoss.getData().get(0).getData();

        Assert.assertEquals(-totalExpenses2019, profitLossData.get(0).getYValue(), delta);
        Assert.assertEquals(-totalExpenses2020, profitLossData.get(1).getYValue(), delta);
        Assert.assertEquals(0, profitLossData.get(2).getYValue(), delta);
        Assert.assertEquals(-totalExpenses2022, profitLossData.get(3).getYValue(), delta);
    }
}