package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Product;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class ProductDaoJpa implements ProductDao {

    private EntityManager em = EmFactory.factory.createEntityManager();

    @Override
    public Product save(Product product) {
        em.getTransaction().begin();
        if (product.isNew()) {
            em.persist(product);
            em.getTransaction().commit();
            return product;
        } else {
            Product updated = em.merge(product);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public Collection<Product> getAll() {
        return em.createNamedQuery(Product.GET_ALL, Product.class).getResultList();
    }

    @Override
    public Collection<Product> getAll(String name) {
        return em.createNamedQuery(Product.GET_ALL_BY_NAME, Product.class)
                .setParameter("name", name).getResultList();
    }

    @Override
    public Product get(long id) {
        List<Product> resList = em.createNamedQuery(Product.GET, Product.class)
                .setParameter("id", id)
                .getResultList();
        return resList.size() == 1 ? resList.get(0) : null;
    }

    @Override
    public boolean delete(long id) {
        em.getTransaction().begin();
        int rows = em.createNamedQuery(Product.DELETE)
                .setParameter("id", id)
                .executeUpdate();
        em.getTransaction().commit();
        return rows != 0;
    }
}
