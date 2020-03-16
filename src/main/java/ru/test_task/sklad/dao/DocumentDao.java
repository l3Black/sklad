package ru.test_task.sklad.dao;

import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;

import java.util.Collection;

public interface DocumentDao {
    //null if not found
    DocEntity save(DocEntity docEntity);

    //null if not found
    DocEntity get(long id);

    Collection<DocEntity> getAll();

    Collection<DocEntity> getByType(DocType type);
}
