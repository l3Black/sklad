package ru.test_task.sklad.service;

import ru.test_task.sklad.dao.StoreDao;
import ru.test_task.sklad.dao.StoreDaoJpa;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.to.ProductTo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.test_task.sklad.util.ValidationUtil.checkNew;
import static ru.test_task.sklad.util.ValidationUtil.checkNotFoundWithId;

public class StoreService {
    private static StoreDao dao = new StoreDaoJpa();
    private static ProductService productService = new ProductService();

    public Collection<ProductTo> balance() {
        Collection<Store> stores = dao.getAll();
        Collection<Product> products = productService.getAll();
        //amount of products in all stores
        Map<Long, Integer> productsSum = stores.stream()
                .flatMap(w -> w.getProducts().entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
        return products.stream()
                .map(p -> new ProductTo(p.getId(), p.getName(), productsSum.get(p.getId())))
                .collect(Collectors.toList());
    }

    public Collection<ProductTo> balance(long id) {
        Store store = get(id);
        Collection<Product> products = productService.getAll();
        return products.stream()
                .filter(p -> store.getProducts().get(p.getId()) != null)
                .map(p -> new ProductTo(p.getId(), p.getName(), store.getProducts().get(p.getId())))
                .collect(Collectors.toList());
    }

    public Collection<Store> getAll() {
        return dao.getAll();
    }

    public Store get(long id) {
        return checkNotFoundWithId(dao.get(id), id);
    }

    public Store create(Store store) {
        Objects.requireNonNull(store, "store must not be null");
        checkNew(store);
        return dao.save(store);
    }

    void update(Store store) {
        Objects.requireNonNull(store, "store must not be null");
        checkNotFoundWithId(dao.save(store), store.getId());
    }

    public void updateName(long id, String name) {
        Objects.requireNonNull(name);
        assert !name.isBlank();
        checkNotFoundWithId(dao.updateName(id, name), id);
    }

    public void delete(long id) {
        checkNotFoundWithId(dao.delete(id), id);
    }

    public void addProducts(long id, Map<Long, Integer> addAmount) {
        dao.addProducts(id, addAmount);
    }

    public void addProducts(long id, long prodId, int amount) {
        Map<Long, Integer> addAmount = new HashMap<>();
        addAmount.put(prodId, amount);
        addProducts(id, addAmount);
    }

    public void reduceProducts(long id, Map<Long, Integer> reduceAmount) {
        dao.reduceProducts(id, reduceAmount);
    }

    public void reduceProducts(long id, long prodId, int amount) {
        Map<Long, Integer> addAmount = new HashMap<>();
        addAmount.put(prodId, amount);
        reduceProducts(id, addAmount);
    }
}
