package ru.test_task.sklad.test_data;

import ru.test_task.sklad.model.Product;

import java.util.Collection;
import java.util.List;

public class ProductTestData {
    public static final long START_SEQ = 100000;

    public static final Product PRODUCT1 = new Product(START_SEQ, "TV", 900f, 1400f);
    public static final Product PRODUCT2 = new Product(START_SEQ + 1, "TabletPC", 800f, 1500f);
    public static final Product PRODUCT3 = new Product(START_SEQ + 2, "Iphone", 500f, 999f);
    public static final Product PRODUCT4 = new Product(START_SEQ + 3, "Laptop", 1000f, 1700f);

    public static Collection<Product> getAllExpected() {
        return List.of(PRODUCT1, PRODUCT2, PRODUCT3, PRODUCT4);
    }

    public static Product getNewProduct() {
        return new Product(null, "PC", 1000f, 1500f);
    }

    public static Product getUpdatedProduct() {
        return new Product(PRODUCT1.getId(), "TV3000", 1200f, 2000f);
    }
}
