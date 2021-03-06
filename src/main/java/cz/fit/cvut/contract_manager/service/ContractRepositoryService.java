package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.ContractState;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.ContractRepository;

public class ContractRepositoryService extends RepositoryService<Integer, Contract, ContractRepository> {
    protected ContractRepositoryService(final ContractRepository repository) {
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

    public Boolean withdraw(final Contract contract) {
        if(!contract.isWithdrawn() && !contract.isTakenOut()) {
            contract.setState(ContractState.WITHDRAWN);
            repository.update(contract);
            return true;
        }

        return false;
    }

    public Boolean takeOut(final Contract contract) {
        if(!contract.isWithdrawn() && !contract.isTakenOut()) {
            contract.setState(ContractState.TAKEN_OUT);
            repository.update(contract);
            return true;
        }

        return false;
    }

    public Boolean expire(final Contract contract) {
        if(contract.isValid()) {
            contract.setState(ContractState.EXPIRED);
            repository.update(contract);
            return true;
        }

        return false;
    }

    public Boolean prolong(final Contract contract, final History history) {
        if (!contract.isWithdrawn() && !contract.isTakenOut() && history.getToDate().compareTo(contract.getExpireDateCurr()) > 0) {
            contract.addHistory(history);
            repository.update(contract);
            return true;
        }

        return false;
    }
}
