package ru.test_task.sklad;

import ru.test_task.sklad.model.Warehouse;
import ru.test_task.sklad.service.AbstractServiceTest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.test_task.sklad.ProductTestData.*;

public class WarehouseTestData extends AbstractServiceTest {
    private static final long START_SEQ = ProductTestData.START_SEQ + ProductTestData.getAllExpected().size();

    private static final Map<Long, Integer> PODOLSK_MAP = new LinkedHashMap<>();
    public final static Warehouse WAREHOUSE1 = new Warehouse(START_SEQ, "podolsk", PODOLSK_MAP);
    private static final Map<Long, Integer> DOMODEDOVO_MAP = new LinkedHashMap<>();
    public final static Warehouse WAREHOUSE2 = new Warehouse(START_SEQ + 1, "domodedovo", DOMODEDOVO_MAP);
    private static final Map<Long, Integer> TROIZK_MAP = new LinkedHashMap<>();
    public final static Warehouse WAREHOUSE3 = new Warehouse(START_SEQ + 2, "troizk", TROIZK_MAP);

    static {
        PODOLSK_MAP.put(PRODUCT1.getId(), 3);
        PODOLSK_MAP.put(PRODUCT2.getId(), 2);
        PODOLSK_MAP.put(PRODUCT3.getId(), 4);
        PODOLSK_MAP.put(PRODUCT4.getId(), 5);
    }

    static {
        //DOMODEDOVO_MAP.put(PRODUCT1.getId(), 0);
        DOMODEDOVO_MAP.put(PRODUCT2.getId(), 2);
        DOMODEDOVO_MAP.put(PRODUCT3.getId(), 1);
        DOMODEDOVO_MAP.put(PRODUCT4.getId(), 3);
    }

    static {
        TROIZK_MAP.put(PRODUCT1.getId(), 13);
        TROIZK_MAP.put(PRODUCT2.getId(), 4);
        TROIZK_MAP.put(PRODUCT3.getId(), 8);
        //TROIZK_MAP.put(PRODUCT4.getId(), 0);
    }

    public static Warehouse addProductExpect(Warehouse w, long productId, int quantity) {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(w.getProducts());
        productsUpd.compute(productId, (k, v) -> v == null ? 0 : v + quantity);
        Warehouse result = new Warehouse(w);
        result.setProducts(productsUpd);
        return result;
    }

    public static Warehouse reduceProductExpect(Warehouse w, long productId, int quantity) {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(w.getProducts());
        productsUpd.computeIfPresent(productId, (k, v) -> v - quantity);
        Warehouse result = new Warehouse(w);
        result.setProducts(productsUpd);
        return result;
    }

    public static Collection<Warehouse> getAllExpect() {
        return List.of(WAREHOUSE1, WAREHOUSE2, WAREHOUSE3);
    }

    public static Warehouse getNewWarehouse() {
        return new Warehouse(null, "moscow", new LinkedHashMap<>());
    }

    public static Warehouse getUpdatedWarehouse() {
        Map<Long, Integer> productsUpd = new LinkedHashMap<>(WAREHOUSE2.getProducts());
        productsUpd.put(PRODUCT2.getId(), 12);
        return new Warehouse(WAREHOUSE2.getId(), "updated", productsUpd);
    }

    public static Warehouse getUpdatedName(Warehouse w, String name) {
        Warehouse updated = new Warehouse(w);
        updated.setName(name);
        return updated;
    }

    public static Warehouse removeProductExpect(Warehouse w, Long id) {
        Warehouse result = new Warehouse(w);
        result.getProducts().remove(id);
        return result;
    }
}
