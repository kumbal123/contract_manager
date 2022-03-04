package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import java.util.List;

public class CustomerRepository extends Repository<Integer, Customer> {
    private static final Logger LOG = Logger.getLogger(CustomerRepository.class);

    public static CustomerRepository getInstance() {
        return CustomerRepository.CustomerRepositoryHolder.INSTANCE;
    }

    @Override
    public Customer getById(final Integer id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        Customer customer = session.get(Customer.class, id);

        trans.commit();
        session.close();

        return customer;
    }

    @Override
    public List<Customer> getAll() {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        List<Customer> customers = (List<Customer>) session.createQuery("from Customer").list();

        trans.commit();
        session.close();

        return customers;
    }

    private static class CustomerRepositoryHolder {

        private static final CustomerRepository INSTANCE = new CustomerRepository();
    }
}
