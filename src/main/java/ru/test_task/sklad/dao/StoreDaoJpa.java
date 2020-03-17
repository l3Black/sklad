package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Store;

import javax.persistence.EntityManager;
import java.util.Collection;

public class StoreDaoJpa implements StoreDao {

    private static final EntityManager em = EmFactory.factory.createEntityManager();

    public static EntityManager getEm() {
        return em;
    }

    @Override
    public Store save(Store store) {
        em.getTransaction().begin();
        if (store.isNew()) {
            em.persist(store);
            em.getTransaction().commit();
            return store;
        } else {
            Store updated = em.merge(store);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public Store updateName(long id, String name) {
        em.getTransaction().begin();
        Store store = em.find(Store.class, id);
        store.setName(name);
        Store result = em.merge(store);
        em.getTransaction().commit();
        return result;
    }

    @Override
    public Store get(long id) {
        return em.find(Store.class, id);
    }

    @Override
    public Collection<Store> getAll() {
        return em.createNamedQuery(Store.GET_ALL, Store.class).getResultList();
    }

    @Override
    public boolean delete(long id) {
        em.getTransaction().begin();
        int rows = em.createNamedQuery(Store.DELETE)
                .setParameter("id", id)
                .executeUpdate();
        em.getTransaction().commit();
        return rows != 0;
    }
}
