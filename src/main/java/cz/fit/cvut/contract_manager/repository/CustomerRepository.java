package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerRepository extends Repository<Integer, Customer> {

    public static CustomerRepository getInstance() {
        return CustomerRepository.CustomerRepositoryHolder.INSTANCE;
    }

    @Override
    public Customer getById(final Integer id) {
        Customer customer = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            customer = session.get(Customer.class, id);
            session.getTransaction().commit();
        } catch(final Exception e) {
            if(session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }

        return customer;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            customers = session.createQuery("from Customer", Customer.class).list();
            session.getTransaction().commit();
        } catch(final Exception e) {
            if(session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }

        return customers;
    }

    @Override
    public void deleteAll() {
        List<Customer> customers = getAll();

        for(Customer customer: customers) {
            deleteByEntity(customer);
        }
    }

    public List<Contract> getContracts(final Integer id) {
        List<Contract> contracts = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();

            Query<Contract> query = session.createQuery("from Contract where customer.id=:dbId", Contract.class);
            query.setParameter("dbId", id);

            contracts = query.list();

            session.getTransaction().commit();
        } catch(final Exception e) {
            if(session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }

        return contracts;
    }

    private static class CustomerRepositoryHolder {

        private static final CustomerRepository INSTANCE = new CustomerRepository();
    }
}
