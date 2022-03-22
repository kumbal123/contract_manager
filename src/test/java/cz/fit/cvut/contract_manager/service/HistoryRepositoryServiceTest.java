package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HistoryRepositoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryRepositoryService historyRepositoryService;

    @Test
    void shouldCreateHistory() {
        History history = new History();
        historyRepositoryService.create(history);
        verify(historyRepository, times(1)).save(history);
    }

    @Test
    void shouldUpdateHistory() {
        History history = new History();
        historyRepositoryService.update(history);
        verify(historyRepository, times(1)).update(history);
    }

    @Test
    void shouldDeleteHistory() {
        History history = new History();
        historyRepositoryService.deleteByEntity(history);
        verify(historyRepository, times(1)).deleteByEntity(history);
    }

}