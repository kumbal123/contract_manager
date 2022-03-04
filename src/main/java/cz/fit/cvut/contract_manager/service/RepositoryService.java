package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.repository.Repository;

import java.util.List;

public abstract class RepositoryService<K, E, R extends Repository<K, E>> implements Service{
    protected final R repository;

    protected RepositoryService(final R repository) {
        this.repository = repository;
    }

    public void create(final E entity) {
        repository.save(entity);
    }

    public void update(final E entity) {
        repository.update(entity);
    }

    public E read(final K id) {
        return repository.getById(id);
    }

    public void deleteByEntity(final E entity) {
        repository.deleteByEntity(entity);
    }

    public List<E> getAll() {
        return repository.getAll();
    }
}
