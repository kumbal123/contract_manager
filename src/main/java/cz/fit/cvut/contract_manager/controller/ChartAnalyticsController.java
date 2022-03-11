package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ChartAnalyticsController extends Controller {

    public TextField fromYearField;
    public TextField toYearField;

    public LineChart<String, Integer> lineChartIncome;
    public Label labelTotalIncome;

    public LineChart<String, Integer> lineChartProfitLoss;
    public Label labelTotalProfitLoss;

    public LineChart<String, Integer> lineChartExpense;
    public Label labelTotalExpenses;

    public Label labelTotalContracts;
    public PieChart pieChart;

    public Pane bottomLeftPane;
    public Pane bottomRightPane;
    public BorderPane mainPane;

    private final ContractRepositoryService contractRepositoryService = ContractRepositoryService.getInstance();

    private List<Contract> contractList;

    private void analyzeContracts(final String year, final List<Contract> contracts) {
        List<XYChart.Data<String, Integer>> listExpensesData = new ArrayList<>();
        List<XYChart.Data<String, Integer>> listIncomeData = new ArrayList<>();
        List<XYChart.Data<String, Integer>> listProfitLossData = new ArrayList<>();

        HashMap<Integer, Integer> monthlyExpenses = new HashMap<>();
        HashMap<Integer, Integer> monthlyIncome = new HashMap<>();

        int totalExpenses = 0, totalIncome = 0, withdrawn = 0, left = 0, totalContracts = contracts.size();

        for(Contract contract: contracts) {
            int month = Util.getMonth(contract.getCreationDate());

            int currExpense = monthlyExpenses.get(month) == null ? 0 : monthlyExpenses.get(month);
            int currIncome = monthlyIncome.get(month) == null ? 0 : monthlyIncome.get(month);
            currIncome += contract.isWithdrawn() ? contract.getTotalPriceCurr() : 0;

            monthlyExpenses.put(month, currExpense + contract.getLendPrice());
            monthlyIncome.put(month, currIncome);

            totalExpenses += contract.getLendPrice();
            totalIncome += contract.isWithdrawn() ? contract.getTotalPriceCurr() : 0;
            withdrawn += contract.isWithdrawn() ? 1 : 0;
            left += contract.isTakenOut() ? 1 : 0;
        }

        for(int i = 0; i < 12; i++) {
            int monthExpense = monthlyExpenses.get(i) == null ? 0 : monthlyExpenses.get(i);
            int yearIncome = monthlyIncome.get(i) == null ? 0 : monthlyIncome.get(i);

            listExpensesData.add(new XYChart.Data<>(Util.months[i], monthExpense));
            listIncomeData.add(new XYChart.Data<>(Util.months[i], yearIncome));
            listProfitLossData.add(new XYChart.Data<>(Util.months[i], yearIncome - monthExpense));
        }

        int stillValid = totalContracts - left - withdrawn;

        labelTotalContracts.setText(String.valueOf(totalContracts));

        if(totalContracts != 0) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Left behind - " + left + " - " + (left * 100/totalContracts) + "%", left),
                new PieChart.Data("Withdrawn - " + withdrawn + " - " + (withdrawn * 100/totalContracts) + "%", withdrawn),
                new PieChart.Data("Still valid - " + stillValid + " - " + (stillValid * 100/totalContracts) + "%", stillValid)
            );

            pieChart.setData(pieChartData);
        } else {
            pieChart.getData().clear();
        }

        ObservableList<XYChart.Data<String, Integer>> lineChartExpenseData = FXCollections.observableArrayList(listExpensesData);
        XYChart.Series<String, Integer> expenseSeries = new XYChart.Series<>(lineChartExpenseData);
        expenseSeries.setName(year);
        lineChartExpense.getData().add(expenseSeries);
        labelTotalExpenses.setText(String.valueOf(totalExpenses));

        ObservableList<XYChart.Data<String, Integer>> lineChartIncomeData = FXCollections.observableArrayList(listIncomeData);
        XYChart.Series<String, Integer> incomeSeries = new XYChart.Series<>(lineChartIncomeData);
        incomeSeries.setName(year);
        lineChartIncome.getData().add(incomeSeries);
        labelTotalIncome.setText(String.valueOf(totalIncome));

        ObservableList<XYChart.Data<String, Integer>> lineChartProfitLossData = FXCollections.observableArrayList(listProfitLossData);
        XYChart.Series<String, Integer> profitLossSeries = new XYChart.Series<>(lineChartProfitLossData);
        profitLossSeries.setName(year);
        lineChartProfitLoss.getData().add(profitLossSeries);
        labelTotalProfitLoss.setText(String.valueOf(totalIncome - totalExpenses));
    }

    private void analyzeContracts(int fromYear, int toYear, final List<Contract> contracts) {
        List<XYChart.Data<String, Integer>> listExpensesData = new ArrayList<>();
        List<XYChart.Data<String, Integer>> listIncomeData = new ArrayList<>();
        List<XYChart.Data<String, Integer>> listProfitLossData = new ArrayList<>();

        HashMap<Integer, Integer> yearlyExpenses = new HashMap<>();
        HashMap<Integer, Integer> yearlyIncome = new HashMap<>();

        int totalExpenses = 0, totalIncome = 0, withdrawn = 0, left = 0, totalContracts = contracts.size();;

        for(Contract contract: contracts) {
            int year = Util.getYear(contract.getCreationDate());
            int currExpense = yearlyExpenses.get(year) == null ? 0 : yearlyExpenses.get(year);
            int currIncome = yearlyIncome.get(year) == null ? 0 : yearlyIncome.get(year);
            currIncome += contract.isWithdrawn() ? contract.getTotalPriceCurr() : 0;

            yearlyExpenses.put(year, currExpense + contract.getLendPrice());
            yearlyIncome.put(year, currIncome);

            totalExpenses += contract.getLendPrice();
            totalIncome += contract.isWithdrawn() ? contract.getTotalPriceCurr() : 0;
            withdrawn += contract.isWithdrawn() ? 1 : 0;
            left += contract.isTakenOut() ? 1 : 0;
        }

        for(int i = fromYear; i <= toYear; i++) {
            int yearExpense = yearlyExpenses.get(i) == null ? 0 : yearlyExpenses.get(i);
            int yearIncome = yearlyIncome.get(i) == null ? 0 : yearlyIncome.get(i);

            listExpensesData.add(new XYChart.Data<>(String.valueOf(i), yearExpense));
            listIncomeData.add(new XYChart.Data<>(String.valueOf(i), yearIncome));
            listProfitLossData.add(new XYChart.Data<>(String.valueOf(i), yearIncome - yearExpense));
        }

        int stillValid = totalContracts - left - withdrawn;

        labelTotalContracts.setText(String.valueOf(totalContracts));

        if(totalContracts != 0) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Left behind - " + left + " - " + (left * 100/totalContracts) + "%", left),
                new PieChart.Data("Withdrawn - " + withdrawn + " - " + (withdrawn * 100/totalContracts) + "%", withdrawn),
                new PieChart.Data("Still valid - " + stillValid + " - " + (stillValid * 100/totalContracts) + "%", stillValid)
            );

            pieChart.setData(pieChartData);
        } else {
            pieChart.getData().clear();
        }


        ObservableList<XYChart.Data<String, Integer>> lineChartExpenseData = FXCollections.observableArrayList(listExpensesData);
        lineChartExpense.getData().add(new XYChart.Series<>(lineChartExpenseData));
        labelTotalExpenses.setText(String.valueOf(totalExpenses));

        ObservableList<XYChart.Data<String, Integer>> lineChartIncomeData = FXCollections.observableArrayList(listIncomeData);
        lineChartIncome.getData().add(new XYChart.Series<>(lineChartIncomeData));
        labelTotalIncome.setText(String.valueOf(totalIncome));

        ObservableList<XYChart.Data<String, Integer>> lineChartProfitLossData = FXCollections.observableArrayList(listProfitLossData);
        lineChartProfitLoss.getData().add(new XYChart.Series<>(lineChartProfitLossData));
        labelTotalProfitLoss.setText(String.valueOf(totalIncome - totalExpenses));
    }

    @FXML
    public void analyzeContractsInMonths(final MouseEvent event) {
        String fromYearStr = fromYearField.getText().trim();

        if(isInteger(fromYearStr)) {
            int fromYear = getInteger(fromYearStr);

            List<Contract> contracts = new ArrayList<>(contractList);
            contracts.removeIf(contract -> Util.getYear(contract.getCreationDate()) != fromYear);

            analyzeContracts(String.valueOf(fromYear), contracts);
        } else {
            Notification.showPopupMessageErr("Enter a valid year", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void analyzeContractsInYears(final MouseEvent event) {
        String fromYearStr = fromYearField.getText().trim();
        String toYearStr = toYearField.getText().trim();

        if(isInteger(fromYearStr) && isInteger(toYearStr)) {
            int fromYear = getInteger(fromYearStr), toYear = getInteger(toYearStr);

            List<Contract> contracts = new ArrayList<>(contractList);
            contracts.removeIf(contract -> Util.getYear(contract.getCreationDate()) < fromYear || Util.getYear(contract.getCreationDate()) > toYear);

            analyzeContracts(fromYear, toYear, contracts);
        } else {
            Notification.showPopupMessageErr("Enter valid years", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void clearCharts(final MouseEvent event) {
        labelTotalContracts.setText("0");
        labelTotalExpenses.setText("0");
        labelTotalIncome.setText("0");
        labelTotalProfitLoss.setText("0");

        pieChart.getData().clear();
        lineChartExpense.getData().clear();
        lineChartIncome.getData().clear();
        lineChartProfitLoss.getData().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contractList = contractRepositoryService.getAll();

        int fromYear = Util.getYear(new Date());

        List<Contract> contracts = new ArrayList<>(contractList);
        contracts.removeIf(contract -> Util.getYear(contract.getCreationDate()) != fromYear);

        analyzeContracts(String.valueOf(fromYear), contracts);
    }
}
