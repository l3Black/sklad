package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Product;

import java.util.Collection;

public interface ProductDao {
    //null if not save
    Product save(Product product);

    Collection<Product> getAll();

    Collection<Product> getAll(String name);

    //null if not found
    Product get(long id);

    //false if not delete
    boolean delete(long id);
}
