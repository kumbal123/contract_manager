package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {
    @Mock
    private ContractRepositoryService service;

    @InjectMocks
    private AnalyticsController controller;

    private Date getDateFromString(final String str) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(str);
    }

    @Test
    void shouldLoadPage() {

    }

    @Test
    @Disabled
    void shouldAnalyzeContracts() throws ParseException {
        List<Contract> contracts = new ArrayList<>();

        contracts.add(new Contract("A1", getDateFromString("01.02.2022"), 1000, getDateFromString("01.03.2022"), "Mobile", "j123", 1280, null));
        contracts.add(new Contract("A3", getDateFromString("20.04.2022"), 1500, getDateFromString("10.06.2022"), "Mobile", "j123", 2265, null));

        contracts.add(new Contract("A2", getDateFromString("10.06.2022"), 2500, getDateFromString("15.08.2022"), "Mobile", "j123", 4150, null));
        contracts.add(new Contract("A4", getDateFromString("28.09.2022"), 3400, getDateFromString("01.11.2022"), "Mobile", "j123", 4556, null));
        contracts.add(new Contract("A5", getDateFromString("04.09.2022"), 5500, getDateFromString("20.09.2022"), "Mobile", "j123", 6380, null));

        int totalContracts = 3, totalExpenses = 2500 + 3400 + 5500, totalIncome = 4150 + 4556 + 6380, profitLoss = -1 * totalExpenses ;

        when(service.getAll()).thenReturn(contracts);

        controller.analyzeContracts(null);

        assertEquals(String.valueOf(totalContracts), controller.labelTotalContracts.getText());
        assertEquals(String.valueOf(totalExpenses), controller.labelExpenses.getText());
        assertEquals(String.valueOf(totalIncome), controller.labelIncome.getText());
        assertEquals(String.valueOf(profitLoss), controller.labelProfitLoss.getText());
    }
}