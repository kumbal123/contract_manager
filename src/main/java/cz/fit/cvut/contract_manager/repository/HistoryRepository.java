package cz.fit.cvut.contract_manager.repository;

import cz.fit.cvut.contract_manager.entity.History;
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
        History history = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            history = session.get(History.class, id);
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

        return history;
    }

    @Override
    public List<History> getAll() {
        List<History> histories = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            histories = session.createQuery("select h from History h join fetch h.contract", History.class).list();
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

        return histories;
    }

    @Override
    public void deleteAll() {
        List<History> histories = getAll();

        for(History history: histories) {
            deleteByEntity(history);
        }
    }

    public List<History> getAllFromContractId(final Integer id) {
        List<History> histories = null;

        try {
            session = FACTORY.openSession();
            session.beginTransaction();

            Query<History> query = session.createQuery("from History where contract.id=:dbId", History.class);
            query.setParameter("dbId", id);

            histories = query.list();

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

        return histories;
    }

    private static class HistoryRepositoryHolder {

        private static final HistoryRepository INSTANCE = new HistoryRepository();
    }
}
