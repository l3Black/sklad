package ru.test_task.sklad.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.test_task.sklad.dao.StoreDao;
import ru.test_task.sklad.dao.StoreDaoJpa;
import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.test_task.sklad.test_data.ProductTestData.*;
import static ru.test_task.sklad.test_data.StoreTestData.*;

public class StoreServiceTest extends AbstractServiceTest {
    StoreService service = new StoreService();
    StoreDao dao = new StoreDaoJpa();

    // It is necessary to clean the entity manager,
    // because the database is filled with test data
    // before each test, which he does not know about.
    @Before
    public void evictCache() {
        StoreDaoJpa.getEm().clear();
    }

    @Test
    public void getAll() {
        Collection<Store> actual = service.getAll();
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(getAllExpect());
    }

    @Test
    public void get() {
        Store actual = service.get(STORE_3.getId());
        assertThat(actual).isEqualToComparingFieldByField(STORE_3);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1);
    }

    @Test
    public void create() {
        Store newStore = getNewStore();
        Store actual = service.create(newStore);
        newStore.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(newStore);
    }

    @Test
    public void update() {
        Store updated = getUpdatedStore();
        service.update(updated);
        assertThat(dao.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void updateName() {
        Store updated = getUpdatedName(STORE_2, "newName");
        service.updateName(updated.getId(), "newName");
        assertThat(dao.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void delete() {
        service.delete(STORE_2.getId());
        Assert.assertNull(dao.get(STORE_2.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(1);
    }

    @Test
    public void addProducts() {
        service.addProducts(STORE_1.getId(), getProdAmount(PRODUCT1.getId(), 3));
        Store actual = dao.get(STORE_1.getId());
        assertThat(actual).isEqualToComparingFieldByField(addProductExpect(STORE_1, PRODUCT1.getId(), 3));
    }

    @Test
    public void addNewProducts() {
        service.addProducts(STORE_2.getId(), getProdAmount(PRODUCT1.getId(), 1));
        Store actual = dao.get(STORE_2.getId());
        assertThat(actual).isEqualToComparingFieldByField(addProductExpect(STORE_2, PRODUCT1.getId(), 1));
    }

    @Test(expected = NotFoundException.class)
    public void addUnknownProduct() {
        service.addProducts(STORE_2.getId(), getProdAmount(12, 2));
    }

    @Test
    public void reduceProducts() {
        service.reduceProducts(STORE_3.getId(), getProdAmount(PRODUCT1.getId(), 3));
        Store actual = dao.get(STORE_3.getId());
        assertThat(actual).isEqualToComparingFieldByField(reduceProductExpect(STORE_3, PRODUCT1.getId(), 3));
    }

    @Test
    public void removeProducts() {
        service.reduceProducts(STORE_1.getId(), getProdAmount(PRODUCT2.getId(), 2));
        Store actual = dao.get(STORE_1.getId());
        assertThat(actual).isEqualToComparingFieldByField(removeProductExpect(STORE_1, PRODUCT2.getId()));
    }

    @Test(expected = OutOfStockException.class)
    public void reduceProductsOutOfStock() {
        service.reduceProducts(STORE_2.getId(), getProdAmount(PRODUCT4.getId(), 80));
    }


    @Test(expected = NotFoundException.class)
    public void reduceUnknownProducts() {
        service.reduceProducts(STORE_2.getId(), getProdAmount(PRODUCT1.getId(), 3));
    }

    @Test
    public void updateNameAndGet() {
        service.getAll();
        Store updated = getUpdatedName(STORE_2, "newName");
        service.updateName(updated.getId(), "newName");
        assertThat(service.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }
}