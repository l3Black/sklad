package ru.test_task.sklad.service;

import org.junit.Before;
import org.junit.Test;
import ru.test_task.sklad.dao.DocumentDaoJpa;
import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.test_task.sklad.test_data.DocTestData.*;

public class DocServiceTest extends AbstractServiceTest {
    DocService docService = new DocService();

    @Before
    public void clearCache() {
        DocumentDaoJpa.getEm().clear();
    }

    @Test
    public void get() {
        DocEntity actual = docService.get(DOC_ENTITY1.getId());
        assertThat(actual).isEqualToComparingFieldByField(DOC_ENTITY1);
    }

    @Test
    public void getAll() {
        Collection<DocEntity> actual = docService.getAll();
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(getAllDocExpect());
    }

    @Test
    public void getByType() {
        Collection<DocEntity> actual = docService.getByType(DocType.MOVE);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(getByTypeExpect(DocType.MOVE));
    }

    @Test
    public void applyEnter() {
        DocEntity actual = docService.apply(DOCUMENT_ENTER);
        DocEntity expect = new DocEntity(DOCUMENT_ENTER);
        expect.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(expect);
    }

    @Test
    public void applyMove() {
        DocEntity actual = docService.apply(DOCUMENT_MOVE);
        DocEntity expect = new DocEntity(DOCUMENT_MOVE);
        expect.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(expect);
    }

    @Test
    public void applySale() {
        DocEntity actual = docService.apply(DOCUMENT_SALE);
        DocEntity expect = new DocEntity(DOCUMENT_SALE);
        expect.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(expect);
    }
}