package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.ContractState;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.ContractRepository;

public class ContractRepositoryService extends RepositoryService<Integer, Contract, ContractRepository> {
    protected ContractRepositoryService(ContractRepository repository) {
        super(repository);
    }

    public static ContractRepositoryService getInstance() {
        return ContractServiceHolder.INSTANCE;
    }

    private static class ContractServiceHolder {
        private static final ContractRepositoryService INSTANCE = new ContractRepositoryService(ContractRepository.getInstance());
    }

    public Contract getMostRecentByContractId(final String contractId) {
        return repository.getMostRecentByContractId(contractId);
    }

    public void removeCustomer(final Contract contract) {
        contract.removeCustomer();
    }

    public void withdraw(final Contract contract) {
        contract.setState(ContractState.WITHDRAWN);
        repository.update(contract);
    }

    public void takeOut(final Contract contract) {
        contract.setState(ContractState.TAKEN_OUT);
        repository.update(contract);
    }

    public void expire(final Contract contract) {
        contract.setState(ContractState.EXPIRED);
        repository.update(contract);
    }

    public void prolong(final Contract contract, final History history) {
        if(contract.getState() != ContractState.WITHDRAWN && contract.getState() != ContractState.TAKEN_OUT) {
            contract.addHistory(history);
            repository.update(contract);
        } else {
            //TODO some shit - prolong method should return Bool - same shit for epixe, takeOUt, withdraw
        }
    }
}
