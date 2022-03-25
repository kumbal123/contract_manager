package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.repository.CustomerRepository;

import java.util.List;

public class CustomerRepositoryService extends RepositoryService<Integer, Customer, CustomerRepository> {

    protected CustomerRepositoryService(final CustomerRepository repository) {
        super(repository);
    }

    public static CustomerRepositoryService getInstance() {
        return CustomerRepositoryService.CustomerServiceHolder.INSTANCE;
    }

    private static class CustomerServiceHolder {
        private static final CustomerRepositoryService INSTANCE = new CustomerRepositoryService(CustomerRepository.getInstance());
    }

    public void assignContract(final Customer customer, final Contract contract) {
        customer.assignContract(contract);
    }

    public List<Contract> getContracts(final Integer id) {
        return repository.getContracts(id);
    }
}
