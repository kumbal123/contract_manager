package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.History;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryRepositoryTest {

    private HistoryRepository historyRepository = HistoryRepository.getInstance();

    @AfterEach
    void tearDown() {
        historyRepository.deleteAll();
    }

    @Test
    void shouldGetAll() {
        List<History> historyList = new ArrayList<>();
        History history1 = new History(1000, new Date(1000), new Date(2234300), null);
        History history2 = new History(10000, new Date(23412234), new Date(34534335), null);

        historyRepository.save(history1);
        historyRepository.save(history2);

        historyList.add(history1);
        historyList.add(history2);

        assertArrayEquals(historyRepository.getAll().toArray(), historyList.toArray());
    }
}