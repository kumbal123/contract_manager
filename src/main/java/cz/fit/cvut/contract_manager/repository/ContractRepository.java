package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.Contract;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

public class ContractRepository implements Repository<Contract> {

    private static final Logger LOG = Logger.getLogger(ContractRepository.class);

    public static ContractRepository getInstance() {
        return ContractRepositoryHolder.INSTANCE;
    }

    @Override
    public void save(Contract obj) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        try {
            session.save(obj);
            LOG.info("Task saved succesfully");
        } catch (Exception e) {
            LOG.error("Error: " + e.getMessage());
        } finally {
            trans.commit();
        }
    }


    private static class ContractRepositoryHolder {

        private static final ContractRepository INSTANCE = new ContractRepository();
    }
}
