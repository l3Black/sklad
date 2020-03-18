package ru.test_task.sklad.dao;

import ru.test_task.sklad.Sklad;
import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class DocumentDaoJpa implements DocumentDao {
    private static EntityManager em = Sklad.factory.createEntityManager();

    @Override
    public DocEntity save(DocEntity docEntity) {
        em.getTransaction().begin();
        em.persist(docEntity);
        em.getTransaction().commit();
        return docEntity;
    }

    @Override
    public DocEntity get(long id) {
        List<DocEntity> res = em.createNamedQuery(DocEntity.GET, DocEntity.class)
                .setParameter("id", id).getResultList();
        return res.size() == 1 ? res.get(0) : null;
    }

    @Override
    public Collection<DocEntity> getAll() {
        return em.createNamedQuery(DocEntity.GET_ALL, DocEntity.class).getResultList();
    }

    @Override
    public Collection<DocEntity> getByType(DocType type) {
        return em.createNamedQuery(DocEntity.GET_BY_TYPE, DocEntity.class)
                .setParameter("type", type).getResultList();
    }

    public static EntityManager getEm() {
        return em;
    }
}
