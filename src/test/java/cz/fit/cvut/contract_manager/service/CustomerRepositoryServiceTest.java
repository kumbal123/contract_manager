package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRepositoryService customerRepositoryService;

    @Test
    void shouldCreateCustomer() {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        customerRepositoryService.create(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void shouldUpdateCustomer() {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        customerRepositoryService.update(customer);
        verify(customerRepository, times(1)).update(customer);
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        customerRepositoryService.deleteByEntity(customer);
        verify(customerRepository, times(1)).deleteByEntity(customer);
    }

    @Test
    void shouldAssignContract() {
        Customer customer = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        Contract contract = new Contract("R12", new Date(20), 1000, new Date(1234567), "Mobile", "j123", 1000, null);

        assertEquals(0, customer.getNumberOfContracts());

        customerRepositoryService.assignContract(customer, contract);

        assertEquals(1, customer.getNumberOfContracts());
        assertEquals(customer, contract.getCustomer());
    }
}