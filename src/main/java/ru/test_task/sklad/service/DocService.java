package ru.test_task.sklad.service;

import ru.test_task.sklad.dao.DocumentDao;
import ru.test_task.sklad.dao.DocumentDaoJpa;
import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;
import ru.test_task.sklad.model.document.Document;
import ru.test_task.sklad.util.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Objects;

import static ru.test_task.sklad.util.ValidationUtil.checkNew;
import static ru.test_task.sklad.util.ValidationUtil.checkNotFoundWithId;

public class DocService {
    private static DocumentDao dao = new DocumentDaoJpa();
    private static WarehouseService warehouseService = new WarehouseService();

    public DocEntity get(long id) {
        return checkNotFoundWithId(dao.get(id), id);
    }

    public Collection<DocEntity> getAll() {
        return dao.getAll();
    }

    public Collection<DocEntity> getByType(DocType type) {
        return dao.getByType(type);
    }

    public DocEntity apply(Document document) {
        switch (document.getType()) {
            case ENTER:
                document.getProducts().forEach((k, v) -> {
                    warehouseService.addProduct(document.getEnterStoreId(), k, v);
                });
                return save(new DocEntity(document));
            case SALE:
                document.getProducts().forEach((k, v) -> {
                    warehouseService.reduceProduct(document.getWriteOffStoreId(), k, v);
                });
                return save(new DocEntity(document));
            case MOVE:
                document.getProducts().forEach((k, v) -> {
                    warehouseService.reduceProduct(document.getWriteOffStoreId(), k, v);
                    warehouseService.addProduct(document.getEnterStoreId(), k, v);
                });
                return save(new DocEntity(document));
            default:
                throw new NotFoundException("unknown document type: " + document.getType());
        }
    }

    DocEntity save(DocEntity docEntity) {
        Objects.requireNonNull(docEntity, "document must not be null");
        checkNew(docEntity);
        return dao.save(docEntity);
    }
}
