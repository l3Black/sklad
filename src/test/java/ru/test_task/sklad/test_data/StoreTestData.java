package ru.test_task.sklad.test_data;

import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.service.AbstractServiceTest;

import java.util.*;

import static ru.test_task.sklad.test_data.ProductTestData.*;

public class StoreTestData extends AbstractServiceTest {
    public static final long START_SEQ = ProductTestData.START_SEQ + ProductTestData.getAllExpected().size();

    private static final Map<Long, Integer> PODOLSK_MAP = new LinkedHashMap<>();
    private static final Map<Long, Integer> DOMODEDOVO_MAP = new LinkedHashMap<>();
    private static final Map<Long, Integer> TROIZK_MAP = new LinkedHashMap<>();

    public final static Store STORE_1 = new Store(START_SEQ, "podolsk", PODOLSK_MAP);
    public final static Store STORE_2 = new Store(START_SEQ + 1, "domodedovo", DOMODEDOVO_MAP);
    public final static Store STORE_3 = new Store(START_SEQ + 2, "troizk", TROIZK_MAP);

    static {
        PODOLSK_MAP.put(PRODUCT1.getId(), 13);
        PODOLSK_MAP.put(PRODUCT2.getId(), 2);
        PODOLSK_MAP.put(PRODUCT3.getId(), 4);
        PODOLSK_MAP.put(PRODUCT4.getId(), 5);
    }

    static {
        DOMODEDOVO_MAP.put(PRODUCT2.getId(), 2);
        DOMODEDOVO_MAP.put(PRODUCT3.getId(), 1);
        DOMODEDOVO_MAP.put(PRODUCT4.getId(), 3);
    }

    static {
        TROIZK_MAP.put(PRODUCT1.getId(), 13);
        TROIZK_MAP.put(PRODUCT2.getId(), 4);
        TROIZK_MAP.put(PRODUCT3.getId(), 8);
    }

    public static Store addProductExpect(Store w, long productId, int quantity) {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(w.getProducts());
        productsUpd.compute(productId, (k, v) -> v == null ? quantity : v + quantity);
        Store result = new Store(w);
        result.setProducts(productsUpd);
        return result;
    }

    public static Store reduceProductExpect(Store w, long productId, int quantity) {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(w.getProducts());
        productsUpd.computeIfPresent(productId, (k, v) -> v - quantity);
        Store result = new Store(w);
        result.setProducts(productsUpd);
        return result;
    }

    public static Collection<Store> getAllExpect() {
        return List.of(STORE_1, STORE_2, STORE_3);
    }

    public static Store getNewWarehouse() {
        return new Store(null, "moscow", new LinkedHashMap<>());
    }

    public static Store getUpdatedWarehouse() {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(STORE_2.getProducts());
        productsUpd.put(PRODUCT2.getId(), 12);
        return new Store(STORE_2.getId(), "updated", productsUpd);
    }

    public static Store getUpdatedName(Store w, String name) {
        Store updated = new Store(w);
        updated.setName(name);
        return updated;
    }

    public static Store removeProductExpect(Store w, Long id) {
        Store result = new Store(w);
        result.getProducts().remove(id);
        return result;
    }

    public static Map<Long, Integer> getProdAmount(long prodId, int quant) {
        Map<Long, Integer> prodAmount = new HashMap<>();
        prodAmount.put(prodId, quant);
        return prodAmount;
    }
}
