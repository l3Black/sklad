package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Store;

import java.util.Collection;

public interface StoreDao {
    //null if not save
    Store save(Store store);

    //null if not found
    Store get(long id);

    Collection<Store> getAll();

    //false if not deleted
    boolean delete(long id);

    //null if not updated
    Store updateName(long id, String name);
}
