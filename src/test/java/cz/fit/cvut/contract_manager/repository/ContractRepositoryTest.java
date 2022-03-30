package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContractRepositoryTest {

    private ContractRepository contractRepository = ContractRepository.getInstance();

    @AfterEach
    void tearDown() {
        contractRepository.deleteAll();
    }

    @Test
    void shouldDeleteAll() {
        Contract contract1 = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        Contract contract2 = new Contract("R12", new Date(123231230), 5000, new Date(220), "Laptop", "q23423", 4000);
        Contract contract3 = new Contract("R12", new Date(1201230), 5000, new Date(2210), "Laptop", "q23423", 4000);

        contractRepository.save(contract1);
        contractRepository.save(contract2);
        contractRepository.save(contract3);
        contractRepository.deleteAll();

        assertEquals(0, contractRepository.getAll().size());
    }

    @Test
    void getAll() {
        List<Contract> contracts = new ArrayList<>();
        Contract contract1 = new Contract("A10", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        Contract contract2 = new Contract("R12", new Date(100), 5000, new Date(220), "Laptop", "q23423", 4000);

        contractRepository.save(contract1);
        contractRepository.save(contract2);

        contracts.add(contract1);
        contracts.add(contract2);

        assertArrayEquals(contracts.toArray(), contractRepository.getAll().toArray());
    }

    @Test
    void getMostRecentByContractId() {
        Contract contract1 = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        Contract contract2 = new Contract("R12", new Date(123231230), 5000, new Date(220), "Laptop", "q23423", 4000);
        Contract contract3 = new Contract("R12", new Date(1201230), 5000, new Date(2210), "Laptop", "q23423", 4000);

        contractRepository.save(contract1);
        contractRepository.save(contract2);
        contractRepository.save(contract3);

        assertEquals(contractRepository.getMostRecentByContractId("R12"), contract2);
    }
}