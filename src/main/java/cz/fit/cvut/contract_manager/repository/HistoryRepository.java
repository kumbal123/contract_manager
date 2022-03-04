package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.History;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

        List<History> history = (List<History>) session.createQuery("from History").list();

        trans.commit();
        session.close();

        return history;
    }

    private static class HistoryRepositoryHolder {

        private static final HistoryRepository INSTANCE = new HistoryRepository();
    }
}
