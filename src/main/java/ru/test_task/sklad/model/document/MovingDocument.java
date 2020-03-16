package ru.test_task.sklad.model.document;

import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Warehouse;

import java.util.Map;

public class MovingDocument extends AbstractDocument {
    private Warehouse entranceWarehouse;
    private Warehouse writeOffWarehouse;

    public MovingDocument(Long id, Map<Product, Integer> products, Warehouse entranceWarehouse, Warehouse writeOffWarehouse) {
        super(id, products);
        this.entranceWarehouse = entranceWarehouse;
        this.writeOffWarehouse = writeOffWarehouse;
    }

    //getters and setters
    public Warehouse getEntranceWarehouse() {
        return entranceWarehouse;
    }

    public void setEntranceWarehouse(Warehouse entranceWarehouse) {
        this.entranceWarehouse = entranceWarehouse;
    }

    public Warehouse getWriteOffWarehouse() {
        return writeOffWarehouse;
    }

    public void setWriteOffWarehouse(Warehouse writeOffWarehouse) {
        this.writeOffWarehouse = writeOffWarehouse;
    }
}
