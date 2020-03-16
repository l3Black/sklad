package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Warehouse;

import java.util.Collection;

public interface WarehouseDao {
    Warehouse save(Warehouse warehouse);

    Warehouse get(long id);

    Collection<Warehouse> getAll();

    boolean delete(long id);

    Warehouse updateName(long id, String name);
}
