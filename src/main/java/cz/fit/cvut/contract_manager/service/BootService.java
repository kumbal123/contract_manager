package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;

import java.util.Date;
import java.util.List;

public class BootService implements Service {

    public static void bootApp() {
        ContractRepositoryService contractRepositoryService = ContractRepositoryService.getInstance();

        List<Contract> contracts = contractRepositoryService.getAll();
        Date today = new Date();

        for(Contract contract : contracts) {
            if(contract.getExpireDateCurr().compareTo(today) < 0) {
                contractRepositoryService.expire(contract);
            }
        }
    }

}
