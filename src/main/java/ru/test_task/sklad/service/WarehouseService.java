package ru.test_task.sklad.service;

import ru.test_task.sklad.dao.WarehouseDao;
import ru.test_task.sklad.dao.WarehouseDaoJpa;
import ru.test_task.sklad.model.Warehouse;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static ru.test_task.sklad.util.ValidationUtil.checkNew;
import static ru.test_task.sklad.util.ValidationUtil.checkNotFoundWithId;

/*I know that this will not work.
I did not find a simple transaction manager and
simply indicated that it would be good to use transactions*/

public class WarehouseService {
    WarehouseDao dao = new WarehouseDaoJpa();
    ProductService productService = new ProductService();

    public Collection<Warehouse> getAll() {
        return dao.getAll();
    }

    public Warehouse get(long id) {
        return checkNotFoundWithId(dao.get(id), id);
    }

    public Warehouse create(Warehouse warehouse) {
        Objects.requireNonNull(warehouse, "warehouse must not be null");
        checkNew(warehouse);
        return dao.save(warehouse);
    }

    @Transactional
        //see comment above
    void update(Warehouse warehouse) {
        Objects.requireNonNull(warehouse, "warehouse must not be null");
        checkNotFoundWithId(dao.save(warehouse), warehouse.getId());
    }

    public void updateName(long id, String name) {
        Objects.requireNonNull(name);
        assert !name.isBlank();
        checkNotFoundWithId(dao.updateName(id, name), id);
    }

    public void delete(long id) {
        checkNotFoundWithId(dao.delete(id), id);
    }

    @Transactional //see comment above
    public void addProduct(long id, long productId, int quantity) {
        Warehouse warehouse = get(id);
        Map<Long, Integer> products = warehouse.getProducts();
        if (products.get(productId) == null) {
            productService.get(productId);
        }
        products.compute(productId, (k, v) -> v == null ? 0 : v + quantity);
        update(warehouse);
    }

    @Transactional //see comment above
    public void reduceProduct(long id, long productId, int quantity) {
        Warehouse warehouse = get(id);
        Map<Long, Integer> products = warehouse.getProducts();
        quantity = Math.abs(quantity);
        Integer productsQuantity = products.get(productId);
        if (productsQuantity == null) {
            throw new NotFoundException("not found productId " + productId + " in warehouse with Id=" + id);
        }
        productsQuantity -= quantity;
        if (productsQuantity < 0) {
            throw new OutOfStockException("Out of stock " + warehouse.getName() + " for productId = " + productId);
        } else if (productsQuantity == 0) {
            products.remove(productId);
        } else {
            products.put(productId, productsQuantity);
        }
        update(warehouse);
    }
}
