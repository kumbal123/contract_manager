package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HistoryRepositoryTest {

    private HistoryRepository historyRepository = HistoryRepository.getInstance();
    private ContractRepository contractRepository = ContractRepository.getInstance();

    @AfterEach
    void tearDown() {
        contractRepository.deleteAll();
    }

    @Test
    void shouldGetAll() {
        Contract contract = new Contract("A10", new Date(10), 1000, new Date(20), "mobile", "", 1000);

        List<History> historyList = new ArrayList<>();
        History history1 = new History(1000, new Date(1000), new Date(2234300), contract);
        History history2 = new History(10000, new Date(23412234), new Date(34534335), contract);

        historyRepository.save(history1);
        historyRepository.save(history2);

        historyList.add(history1);
        historyList.add(history2);

        assertArrayEquals(historyRepository.getAll().toArray(), historyList.toArray());
    }
}