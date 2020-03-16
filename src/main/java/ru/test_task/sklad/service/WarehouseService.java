package ru.test_task.sklad.service;

import ru.test_task.sklad.dao.WarehouseDao;
import ru.test_task.sklad.dao.WarehouseDaoJpa;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Warehouse;
import ru.test_task.sklad.to.ProductTo;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.test_task.sklad.util.ValidationUtil.checkNew;
import static ru.test_task.sklad.util.ValidationUtil.checkNotFoundWithId;

public class WarehouseService {
    private static WarehouseDao dao = new WarehouseDaoJpa();
    private static ProductService productService = new ProductService();

    public Collection<ProductTo> balance() {
        Collection<Warehouse> warehouses = dao.getAll();
        Collection<Product> products = productService.getAll();
        //quantity of products in all warehouses
        Map<Long, Integer> productsSum = warehouses.stream()
                .map(w -> w.getProducts().entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));

        return products.stream()
                .map(p -> new ProductTo(p.getId(), p.getName(), productsSum.get(p.getId())))
                .collect(Collectors.toList());
    }

    public Collection<ProductTo> balance(long id) {
        Warehouse warehouse = get(id);
        Collection<Product> products = productService.getAll();
        return products.stream()
                .filter(p -> warehouse.getProducts().get(p.getId()) != null)
                .map(p -> new ProductTo(p.getId(), p.getName(), warehouse.getProducts().get(p.getId())))
                .collect(Collectors.toList());
    }

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

    public void addProduct(long id, long productId, int quantity) {
        Warehouse warehouse = get(id);
        Map<Long, Integer> products = warehouse.getProducts();
        if (products.get(productId) == null) {
            productService.get(productId);
        }
        products.compute(productId, (k, v) -> v == null ? quantity : v + quantity);
        update(warehouse);
    }

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
