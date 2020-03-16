package ru.test_task.sklad.model.document;

import java.util.Map;

public class Document {
    private DocType type;
    private Long enterStoreId;
    private Long writeOffStoreId;
    private Map<Long, Integer> products;

    public Document(DocType type, Long enterStoreId, Long writeOffStoreId, Map<Long, Integer> products) {
        this.type = type;
        this.enterStoreId = enterStoreId;
        this.writeOffStoreId = writeOffStoreId;
        this.products = products;
    }


    @Override
    public String toString() {
        return "Document{" +
                "type=" + type +
                ", entranceWarehouseId=" + enterStoreId +
                ", writeOffWarehouseId=" + writeOffStoreId +
                ", products=" + products +
                '}';
    }

    //Getters and Setters
    public DocType getType() {
        return type;
    }

    public void setType(DocType type) {
        this.type = type;
    }

    public Long getEnterStoreId() {
        return enterStoreId;
    }

    public void setEnterStoreId(Long enterStoreId) {
        this.enterStoreId = enterStoreId;
    }

    public Long getWriteOffStoreId() {
        return writeOffStoreId;
    }

    public void setWriteOffStoreId(Long writeOffStoreId) {
        this.writeOffStoreId = writeOffStoreId;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }
}
