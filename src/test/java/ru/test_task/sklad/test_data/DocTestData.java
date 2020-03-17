package ru.test_task.sklad.test_data;

import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;
import ru.test_task.sklad.model.document.Document;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.test_task.sklad.test_data.ProductTestData.*;

public class DocTestData {
    public static final long START_SEQ = StoreTestData.START_SEQ + StoreTestData.getAllExpect().size();

    private static Map<Long, Integer> enterProducts = new LinkedHashMap<>();
    static {
        enterProducts.put(PRODUCT1.getId(), 12);
        enterProducts.put(PRODUCT2.getId(), 13);
        enterProducts.put(PRODUCT3.getId(), 14);
        enterProducts.put(PRODUCT4.getId(), 15);
    }
    private static Map<Long, Integer> saleProducts = new LinkedHashMap<>();
    static {
        saleProducts.put(PRODUCT1.getId(), 2);
        saleProducts.put(PRODUCT2.getId(), 1);
        saleProducts.put(PRODUCT3.getId(), 3);
        saleProducts.put(PRODUCT4.getId(), 2);
    }
    private static Map<Long, Integer> moveProducts = new LinkedHashMap<>();
    static {
        moveProducts.put(PRODUCT1.getId(), 2);
        moveProducts.put(PRODUCT2.getId(), 1);
        moveProducts.put(PRODUCT3.getId(), 1);
        moveProducts.put(PRODUCT4.getId(), 3);
    }

    public static final DocEntity DOC_ENTITY1 = new DocEntity(START_SEQ, DocType.ENTER, 100006L, null, enterProducts);
    public static final DocEntity DOC_ENTITY2 = new DocEntity(START_SEQ + 1, DocType.SALE, null, 100004L, saleProducts);
    public static final DocEntity DOC_ENTITY3 = new DocEntity(START_SEQ + 2, DocType.MOVE, 100005L, 100004L, moveProducts);

    public static final Document DOCUMENT_MOVE = new Document(DocType.MOVE, 100005L, 100004L, moveProducts);
    public static final Document DOCUMENT_SALE = new Document(DocType.SALE, null, 100004L, saleProducts);
    public static final Document DOCUMENT_ENTER = new Document(DocType.ENTER, 100006L, null, enterProducts);


    public static Collection<DocEntity> getAllDocExpect() {
        return List.of(DOC_ENTITY1, DOC_ENTITY2, DOC_ENTITY3);
    }

    public static Collection<DocEntity> getByTypeExpect(DocType type) {
        switch (type) {
            case MOVE:
                return List.of(DOC_ENTITY3);
            case SALE:
                return List.of(DOC_ENTITY2);
            default:
                return List.of(DOC_ENTITY1);
        }
    }
}
