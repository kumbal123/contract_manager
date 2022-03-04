package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BootService implements Service {

    public static void bootApp() throws ParseException {
        ContractRepositoryService contractRepositoryService = ContractRepositoryService.getInstance();

        List<Contract> contracts = contractRepositoryService.getAll();
        Date today = new SimpleDateFormat("dd.MM.yyyy").parse("20.09.2022");

        for(Contract contract : contracts) {
            if(contract.getExpireDateCurr().compareTo(today) < 0) {
                contractRepositoryService.expire(contract);
            }
        }
    }

}
