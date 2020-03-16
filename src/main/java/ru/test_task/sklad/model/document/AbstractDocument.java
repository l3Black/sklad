package ru.test_task.sklad.model.document;

import ru.test_task.sklad.model.AbstractBaseEntity;
import ru.test_task.sklad.model.Product;

import java.util.Map;

public abstract class AbstractDocument extends AbstractBaseEntity {

    private Map<Product, Integer> products;

    public AbstractDocument(Long id, Map<Product, Integer> products) {
        super(id);
        this.products = products;
    }

    //getter and setters
    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }
}
