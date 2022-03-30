package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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

        List<Customer> customers = session.createQuery("from Customer", Customer.class).list();

        trans.commit();
        session.close();

        return customers;
    }

    @Override
    public void deleteAll() {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        session.createQuery("delete Customer").executeUpdate();
        trans.commit();
        session.close();
    }

    public Integer getNumberOfContracts(final Contract contract) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();


        contract.getNumberOfProlongs();

        trans.commit();
        session.close();

        return 1;
    }

    public List<Contract> getContracts(final Integer id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        Query<Contract> query = session.createQuery("from Contract where customer.id=:dbId", Contract.class);
        query.setParameter("dbId", id);

        List<Contract> contracts = query.list();


        trans.commit();
        session.close();

        return contracts;
    }

    private static class CustomerRepositoryHolder {

        private static final CustomerRepository INSTANCE = new CustomerRepository();
    }
}
