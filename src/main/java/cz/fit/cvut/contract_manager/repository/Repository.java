package cz.fit.cvut.contract_manager.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public abstract class Repository<K, E> {
    protected Session session;
    protected SessionFactory FACTORY = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();

    public void save(final E entity) {
        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            session.save(entity);
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
    }

    public void update(final E entity) {
        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            session.update(entity);
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
    }

    public void deleteByEntity(final E entity) {
        try {
            session = FACTORY.openSession();
            session.beginTransaction();
            session.delete(entity);
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
    }

    public abstract E getById(final K id);
    public abstract List<E> getAll();
    public abstract void deleteAll();
}
