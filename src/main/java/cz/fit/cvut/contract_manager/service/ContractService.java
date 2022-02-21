package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.repository.ContractRepository;

import java.util.Date;

public class ContractService {
    private final ContractRepository repo = ContractRepository.getInstance();

    public static ContractService getInstance() {
        return ContractServiceHolder.INSTANCE;
    }

    private static class ContractServiceHolder {

        private static final ContractService INSTANCE = new ContractService();
    }

    public void saveContract(String id, Integer price) {
        Contract task = new Contract(id, price);
        repo.save(task);
    }
}
