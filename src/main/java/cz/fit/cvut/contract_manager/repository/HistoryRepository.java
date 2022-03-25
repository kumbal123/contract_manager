package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.History;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

public class HistoryRepository extends Repository<Integer, History> {
    private static final Logger LOG = Logger.getLogger(HistoryRepository.class);

    public static HistoryRepository getInstance() {
        return HistoryRepository.HistoryRepositoryHolder.INSTANCE;
    }

    @Override
    public History getById(final Integer id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        History history = session.get(History.class, id);

        trans.commit();
        session.close();

        return history;
    }

    @Override
    public List<History> getAll() {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        List<History> history = session.createQuery("from History", History.class).list();

        trans.commit();
        session.close();

        return history;
    }

    @Override
    public void deleteAll() {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        session.createQuery("delete History").executeUpdate();
        trans.commit();
        session.close();
    }

    public List<History> getAllFromContractId(final Integer id) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();

        Query<History> query = session.createQuery("from History where contract.id=:dbId", History.class);
        query.setParameter("dbId", id);

        List<History> history = query.list();

        trans.commit();
        session.close();

        return history;
    }

    private static class HistoryRepositoryHolder {

        private static final HistoryRepository INSTANCE = new HistoryRepository();
    }
}
