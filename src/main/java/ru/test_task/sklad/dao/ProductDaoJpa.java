package ru.test_task.sklad.dao;

import ru.test_task.sklad.Sklad;
import ru.test_task.sklad.model.Product;

import javax.persistence.EntityManager;
import java.util.Collection;

public class ProductDaoJpa implements ProductDao {

    private static final EntityManager em = Sklad.factory.createEntityManager();

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

    public static EntityManager getEm() {
        return em;
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

    @Override
    public Product get(long id) {
        return em.find(Product.class, id);
    }
}
