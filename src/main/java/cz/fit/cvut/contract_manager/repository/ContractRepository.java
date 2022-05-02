package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContractRepository extends Repository<Integer, Contract> {

    public static ContractRepository getInstance() {
        return ContractRepositoryHolder.INSTANCE;
    }

    public Contract getMostRecentByContractId(final String id) {
        List<Contract> contracts = new ArrayList<>();

        try {
            session = FACTORY.openSession();
            session.beginTransaction();

            Query<Contract> query = session.createQuery("select c from Contract c join fetch c.customer where c.contractId=:contractId", Contract.class);
            query.setParameter("contractId", id);

            contracts = query.list();
            contracts.sort(Comparator.comparing(Contract::getCreationDate).reversed());

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

        return contracts.isEmpty() ? null : contracts.get(0);
    }

    @Override
    public Contract getById(final Integer id) {
        Contract contract = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();

            contract = session.get(Contract.class, id);

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

        return contract;
    }

    @Override
    public List<Contract> getAll() {
        List<Contract> contracts = new ArrayList<>();

        try {
            session = FACTORY.openSession();
            session.beginTransaction();

            contracts = session.createQuery("select c from Contract c join fetch c.customer", Contract.class).list();

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

    @Override
    public void deleteAll() {
        List<Contract> contracts = getAll();

        for(Contract contract : contracts) {
            deleteByEntity(contract);
        }
    }


    private static class ContractRepositoryHolder {

        private static final ContractRepository INSTANCE = new ContractRepository();
    }
}
