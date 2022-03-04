package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.HistoryRepository;

public class HistoryRepositoryService extends RepositoryService<Integer, History, HistoryRepository> {
    protected HistoryRepositoryService(HistoryRepository repository) {
        super(repository);
    }

    public static HistoryRepositoryService getInstance() {
        return HistoryRepositoryService.HistoryServiceHolder.INSTANCE;
    }

    private static class HistoryServiceHolder {
        private static final HistoryRepositoryService INSTANCE = new HistoryRepositoryService(HistoryRepository.getInstance());
    }
}
