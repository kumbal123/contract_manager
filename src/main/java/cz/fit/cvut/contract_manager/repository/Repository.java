package cz.fit.cvut.contract_manager.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public abstract class Repository<K, E> {
    SessionFactory FACTORY = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();

    public void save(final E entity) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        session.save(entity);
        trans.commit();
        session.close();
    }

    public void update(final E entity) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        session.update(entity);
        trans.commit();
        session.close();
    }

    public void deleteByEntity(final E entity) {
        Session session = FACTORY.openSession();
        Transaction trans = session.beginTransaction();
        session.delete(entity);
        trans.commit();
        session.close();
    }

    public abstract E getById(final K id);
    public abstract List<E> getAll();
}
