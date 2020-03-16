package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.Product;

import java.util.Collection;

public interface ProductDao {
    Product save(Product product);

    Collection<Product> getAll();

    Collection<Product> getAll(String name);

    Product get(long id);

    boolean delete(long id);
}
