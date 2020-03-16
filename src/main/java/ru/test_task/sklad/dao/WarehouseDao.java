package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Warehouse;

import java.util.Collection;

public interface WarehouseDao {
    //null if not save
    Warehouse save(Warehouse warehouse);

    //null if not found
    Warehouse get(long id);

    Collection<Warehouse> getAll();

    //false if not deleted
    boolean delete(long id);

    //null if not updated
    Warehouse updateName(long id, String name);
}
