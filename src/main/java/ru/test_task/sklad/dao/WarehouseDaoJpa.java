package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Warehouse;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class WarehouseDaoJpa implements WarehouseDao {

    private EntityManager em = EmFactory.factory.createEntityManager();

    @Override
    public Warehouse save(Warehouse warehouse) {
        em.getTransaction().begin();
        if (warehouse.isNew()) {
            em.persist(warehouse);
            em.getTransaction().commit();
            return warehouse;
        } else {
            Warehouse updated = em.merge(warehouse);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public Warehouse updateName(long id, String name) {
        em.getTransaction().begin();
        Warehouse warehouse = em.find(Warehouse.class, id);
        warehouse.setName(name);
        Warehouse result = em.merge(warehouse);
        em.getTransaction().commit();
        return result;
    }

    @Override
    public Warehouse get(long id) {
        List<Warehouse> resList = em.createNamedQuery(Warehouse.GET, Warehouse.class)
                .setParameter("id", id)
                .getResultList();
        return resList.size() == 1 ? resList.get(0) : null;
    }

    @Override
    public Collection<Warehouse> getAll() {
        return em.createNamedQuery(Warehouse.GET_ALL, Warehouse.class).getResultList();
    }

    @Override
    public boolean delete(long id) {
        em.getTransaction().begin();
        int rows = em.createNamedQuery(Warehouse.DELETE)
                .setParameter("id", id)
                .executeUpdate();
        em.getTransaction().commit();
        return rows != 0;
    }

}
