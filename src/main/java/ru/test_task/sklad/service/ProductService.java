package ru.test_task.sklad.service;

import ru.test_task.sklad.dao.ProductDao;
import ru.test_task.sklad.dao.ProductDaoJpa;
import ru.test_task.sklad.model.Product;

import java.util.Collection;
import java.util.Objects;

import static ru.test_task.sklad.util.ValidationUtil.*;

public class ProductService {
    private static ProductDao dao = new ProductDaoJpa();

    public Product create(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        checkNew(product);
        return dao.save(product);
    }

    public Collection<Product> getAll() {
        return dao.getAll();
    }

    public Collection<Product> getAll(String name) {
        Collection<Product> result = dao.getAll(name);
        checkNotFound(!result.isEmpty(), " name = " + name);
        return result;
    }

    public Product get(long id) {
        return checkNotFoundWithId(dao.get(id), id);
    }

    public void update(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        checkNotFoundWithId(dao.save(product), product.getId());
    }

    public void delete(long id) {
        checkNotFoundWithId(dao.delete(id), id);
    }
}
