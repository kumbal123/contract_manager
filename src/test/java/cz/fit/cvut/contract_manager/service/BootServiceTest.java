package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.repository.ContractRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BootServiceTest {

    private ContractRepository contractRepository = ContractRepository.getInstance();

    @AfterEach
    void tearDown() {
        contractRepository.deleteAll();
    }

    @Test
    void shouldExpireContractsThatAreDue() {
        long today = new Date().getTime();

        Contract contract1 = new Contract("A10", new Date(10), 1000, new Date(20), "mobile", "", 1000);
        Contract contract2 = new Contract("R12", new Date(100), 5000, new Date(220), "phone", "", 4000);
        Contract contract3 = new Contract("B01", new Date(100), 5000, new Date(today + 1002340), "phone", "", 4000);
        Contract contract4 = new Contract("K99", new Date(100), 5000, new Date(today + 12321), "phone", "", 4000);

        contractRepository.save(contract1);
        contractRepository.save(contract2);
        contractRepository.save(contract3);
        contractRepository.save(contract4);

        assertTrue(contract1.isValid());
        assertTrue(contract2.isValid());
        assertTrue(contract3.isValid());
        assertTrue(contract4.isValid());

        BootService.bootApp();

        assertTrue(contractRepository.getMostRecentByContractId("A10").isExpired());
        assertTrue(contractRepository.getMostRecentByContractId("R12").isExpired());
        assertTrue(contractRepository.getMostRecentByContractId("B01").isValid());
        assertTrue(contractRepository.getMostRecentByContractId("K99").isValid());
    }
}