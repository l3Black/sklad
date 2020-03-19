package ru.test_task.sklad.to;

import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;

import java.util.Map;

public class DocumentTo {
    private final Long id;
    private final DocType type;
    private final Long enterStoreId;
    private final Long writeOffStoreId;
    private final Map<Long, Integer> products;

    public DocumentTo(DocEntity doc){
        this.id = doc.getId();
        this.type = doc.getType();
        this.enterStoreId = doc.getEnterStoreId();
        this.writeOffStoreId = doc.getWriteOffStoreId();
        this.products = doc.getProducts();
    }
}
