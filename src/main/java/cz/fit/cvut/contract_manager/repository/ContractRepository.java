package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;

public class ContractRepository extends Repository<Integer, Contract> {

    private static final Logger LOG = Logger.getLogger(ContractRepository.class);

    public static ContractRepository getInstance() {
        return ContractRepositoryHolder.INSTANCE;
    }

    public Contract getMostRecentByContractId(final String id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        Query query = session.createQuery("from Contract where contractId=:contractId");
        query.setParameter("contractId", id);

        List<Contract> contracts = (List<Contract>) query.list();
        contracts.sort(Comparator.comparing(Contract::getCreationDate).reversed());

        trans.commit();
        session.close();

        return contracts.isEmpty() ? null : contracts.get(0);
    }

    @Override
    public Contract getById(final Integer id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        Contract contract = session.get(Contract.class, id);

        trans.commit();
        session.close();

        return contract;
    }

    @Override
    public List<Contract> getAll() {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        List<Contract> contracts = (List<Contract>) session.createQuery("from Contract").list();

        trans.commit();
        session.close();

        return contracts;
    }


    private static class ContractRepositoryHolder {

        private static final ContractRepository INSTANCE = new ContractRepository();
    }
}
