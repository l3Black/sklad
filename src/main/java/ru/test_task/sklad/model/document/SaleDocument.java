package ru.test_task.sklad.model.document;

import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Warehouse;

import java.util.Map;

public class SaleDocument extends AbstractDocument {
    private Warehouse writeOffWarehouse;

    public SaleDocument(Long id, Map<Product, Integer> products, Warehouse writeOffWarehouse) {
        super(id, products);
        this.writeOffWarehouse = writeOffWarehouse;
    }

    //getters and setters
    public Warehouse getWriteOffWarehouse() {
        return writeOffWarehouse;
    }

    public void setWriteOffWarehouse(Warehouse writeOffWarehouse) {
        this.writeOffWarehouse = writeOffWarehouse;
    }
}
