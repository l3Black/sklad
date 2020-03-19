package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Store;

import java.util.Collection;
import java.util.Map;

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

    void reduceProducts(long id, Map<Long, Integer> reduceAmount);

    void addProducts(long id, Map<Long, Integer> addAmount);
}
