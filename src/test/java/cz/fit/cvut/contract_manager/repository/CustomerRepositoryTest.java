package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerRepositoryTest {

    private CustomerRepository customerRepository = CustomerRepository.getInstance();

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldGetAll() {
        List<Customer> expectedCustomers = new ArrayList<>();
        Customer customer1 = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        Customer customer2 = new Customer("Annie", "f", "Prague", "slow1", "velocity", "jjg435li", "afl44l", "V", "vn", new Date(4432342));

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        expectedCustomers.add(customer1);
        expectedCustomers.add(customer2);

        assertArrayEquals(expectedCustomers.toArray(), customerRepository.getAll().toArray());
    }

    @Test
    void shouldDeleteAll() {
        Customer customer1 = new Customer("Mike", "m", "Prague", "fast1", "velocity", "123l123", "a24234", "V", "vn", new Date(332342342));
        Customer customer2 = new Customer("Annie", "f", "Prague", "slow1", "velocity", "jjg435li", "afl44l", "V", "vn", new Date(4432342));

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.deleteAll();

        assertEquals(0, customerRepository.getAll().size());
    }
}