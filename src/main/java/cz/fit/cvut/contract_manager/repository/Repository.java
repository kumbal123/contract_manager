package cz.fit.cvut.contract_manager.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface Repository<T> {
    SessionFactory FACTORY = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();

    void save(T obj);
}
