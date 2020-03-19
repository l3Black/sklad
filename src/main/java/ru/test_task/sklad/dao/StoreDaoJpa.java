package ru.test_task.sklad.dao;

import ru.test_task.sklad.Sklad;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Map;

public class StoreDaoJpa implements StoreDao {

    private static final EntityManager em = Sklad.factory.createEntityManager();

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
        em.getTransaction().commit();
        return store;
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

    @Override
    public void reduceProducts(long id, Map<Long, Integer> reduceAmount) {
        em.getTransaction().begin();
        Store store = em.find(Store.class, id);
        Map<Long, Integer> products = store.getProducts();
        reduceAmount.forEach((prodId, amount) -> {
            Integer realAmount = products.get(prodId);
            if (realAmount == null) {
                em.getTransaction().rollback();
                throw new NotFoundException("not found productId " + prodId + " in store with Id=" + id);
            }
            amount = Math.abs(amount);
            realAmount -= amount;
            if (realAmount < 0) {
                em.getTransaction().rollback();
                throw new OutOfStockException("Out of stock " + store.getName() + " for productId = " + prodId);
            } else if (realAmount == 0) {
                products.remove(prodId);
            } else {
                products.put(prodId, realAmount);
            }
        });
        em.getTransaction().commit();
    }

    @Override
    public void addProducts(long id, Map<Long, Integer> addAmount) {
        em.getTransaction().begin();
        Store store = em.find(Store.class, id);
        Map<Long, Integer> products = store.getProducts();
        addAmount.forEach((prodId, amount) -> {
            if (products.get(prodId) == null && em.find(Product.class, prodId) == null) {
                em.getTransaction().rollback();
                throw new NotFoundException("Unknown product with id: " + prodId);
            }
            int absAmount = Math.abs(amount);
            products.compute(prodId, (k, v) -> v == null ? absAmount : v + absAmount);
        });
        em.getTransaction().commit();
    }

    public static EntityManager getEm() {
        return em;
    }
}
