package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.HistoryRepository;

import java.util.List;

public class HistoryRepositoryService extends RepositoryService<Integer, History, HistoryRepository> {
    protected HistoryRepositoryService(final HistoryRepository repository) {
        super(repository);
    }

    public static HistoryRepositoryService getInstance() {
        return HistoryRepositoryService.HistoryServiceHolder.INSTANCE;
    }

    private static class HistoryServiceHolder {
        private static final HistoryRepositoryService INSTANCE = new HistoryRepositoryService(HistoryRepository.getInstance());
    }

    public List<History> getAllFromContractId(final Integer id) {
        return repository.getAllFromContractId(id);
    }
}
