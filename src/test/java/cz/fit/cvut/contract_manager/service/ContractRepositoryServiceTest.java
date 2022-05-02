package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.ContractState;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractRepositoryServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractRepositoryService contractRepositoryService;

    @Test
    void shouldCreateContract() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        contractRepositoryService.create(contract);
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void shouldUpdateContract() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        contractRepositoryService.update(contract);
        verify(contractRepository, times(1)).update(contract);
    }

    @Test
    void shouldDeleteContract() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        contractRepositoryService.deleteByEntity(contract);
        verify(contractRepository, times(1)).deleteByEntity(contract);
    }

    @Test
    void shouldGetMostRecentByContractId() {
        String contractId = "R12";
        Contract expectedContract = new Contract(contractId, new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        when(contractRepository.getMostRecentByContractId(contractId)).thenReturn(expectedContract);

        Contract actualContract = contractRepositoryService.getMostRecentByContractId("R12");
        verify(contractRepository, times(1)).getMostRecentByContractId(contractId);

        assertEquals(actualContract, expectedContract);
    }

    @Test
    void shouldWithdraw() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(1)).update(contract);
    }

    @Test
    void shouldTakeOut() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(1)).update(contract);
    }

    @Test
    void shouldExpire() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.expire(contract));
        verify(contractRepository, times(1)).update(contract);
    }

    @Test
    void shouldNotWithdrawWhenAlreadyWithdrawnOrTakenOut() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(1)).update(contract);

        assertFalse(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(1)).update(contract);

        contract.setState(ContractState.VALID);

        assertTrue(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(2)).update(contract);

        assertFalse(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(2)).update(contract);
    }

    @Test
    void shouldNotTakeOutWhenAlreadyTakenOutOrWithdrawn() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(1)).update(contract);

        assertFalse(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(1)).update(contract);

        contract.setState(ContractState.VALID);

        assertTrue(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(2)).update(contract);

        assertFalse(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(2)).update(contract);
    }

    @Test
    void shouldNotExpireWhenAlreadyExpiredOrWithdrawnOrTakenOut() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);

        assertTrue(contractRepositoryService.expire(contract));
        verify(contractRepository, times(1)).update(contract);

        assertFalse(contractRepositoryService.expire(contract));
        verify(contractRepository, times(1)).update(contract);

        contract.setState(ContractState.VALID);

        assertTrue(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(2)).update(contract);

        assertFalse(contractRepositoryService.expire(contract));
        verify(contractRepository, times(2)).update(contract);

        contract.setState(ContractState.VALID);

        assertTrue(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(3)).update(contract);

        assertFalse(contractRepositoryService.expire(contract));
        verify(contractRepository, times(3)).update(contract);
    }

    @Test
    void shouldProlong() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        History history = new History(contract.getTotalPriceCurr(), contract.getExpireDateCurr(), new Date(11000), contract);

        assertTrue(contractRepositoryService.prolong(contract, history));
        verify(contractRepository, times(1)).update(contract);
    }

    @Test
    void shouldNotProlongWhenAlreadyWithdrawnOrTakenOut() {
        Contract contract = new Contract("R12", new Date(10), 1000, new Date(20), "Mobile", "j123", 1000);
        History history = new History(contract.getTotalPriceCurr(), contract.getExpireDateCurr(), new Date(11000), contract);

        assertTrue(contractRepositoryService.withdraw(contract));
        verify(contractRepository, times(1)).update(contract);
        assertFalse(contractRepositoryService.prolong(contract, history));
        verify(contractRepository, times(1)).update(contract);

        contract.setState(ContractState.VALID);

        assertTrue(contractRepositoryService.takeOut(contract));
        verify(contractRepository, times(2)).update(contract);

        assertFalse(contractRepositoryService.prolong(contract, history));
        verify(contractRepository, times(2)).update(contract);
    }

    @Test
    void shouldNotProlongWhenProlongDateIsInThePast() {
        Contract contract = new Contract("R12", new Date(20), 1000, new Date(1234567), "Mobile", "j123", 1000);
        History history = new History(contract.getTotalPriceCurr(), contract.getExpireDateCurr(), new Date(111000), contract);

        assertFalse(contractRepositoryService.prolong(contract, history));
        verify(contractRepository, times(0)).update(contract);
    }
}