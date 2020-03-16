package ru.test_task.sklad.model.document;

import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Warehouse;

import java.util.Map;

public class EntranceDocument extends AbstractDocument {
    private Warehouse entranceWarehouse;

    public EntranceDocument(Long id, Map<Product, Integer> products, Warehouse entranceWarehouse) {
        super(id, products);
        this.entranceWarehouse = entranceWarehouse;
    }

    //getters and setters
    public Warehouse getEntranceWarehouse() {
        return entranceWarehouse;
    }

    public void setEntranceWarehouse(Warehouse entranceWarehouse) {
        this.entranceWarehouse = entranceWarehouse;
    }
}
