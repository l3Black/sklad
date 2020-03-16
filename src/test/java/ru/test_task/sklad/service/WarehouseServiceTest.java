package ru.test_task.sklad.service;

import org.junit.Assert;
import org.junit.Test;
import ru.test_task.sklad.dao.WarehouseDao;
import ru.test_task.sklad.dao.WarehouseDaoJpa;
import ru.test_task.sklad.model.Warehouse;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.test_task.sklad.ProductTestData.*;
import static ru.test_task.sklad.WarehouseTestData.*;

public class WarehouseServiceTest extends AbstractServiceTest {
    WarehouseService service = new WarehouseService();
    WarehouseDao dao = new WarehouseDaoJpa();

    @Test
    public void getAll() {
        Collection<Warehouse> actual = service.getAll();
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(getAllExpect());
    }

    @Test
    public void get() {
        Warehouse actual = service.get(WAREHOUSE3.getId());
        assertThat(actual).isEqualToComparingFieldByField(WAREHOUSE3);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1);
    }

    @Test
    public void create() {
        Warehouse newWarehouse = getNewWarehouse();
        Warehouse actual = service.create(newWarehouse);
        newWarehouse.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(newWarehouse);
    }

    @Test
    public void update() {
        Warehouse updated = getUpdatedWarehouse();
        service.update(updated);
        assertThat(dao.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void updateName() {
        Warehouse updated = getUpdatedName(WAREHOUSE2, "newName");
        service.updateName(updated.getId(), "newName");
        assertThat(dao.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void delete() {
        service.delete(WAREHOUSE2.getId());
        Assert.assertNull(dao.get(WAREHOUSE2.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(1);
    }

    @Test
    public void addProduct() {
        service.addProduct(WAREHOUSE1.getId(), PRODUCT1.getId(), 3);
        Warehouse actual = dao.get(WAREHOUSE1.getId());
        assertThat(actual).isEqualToComparingFieldByField(addProductExpect(WAREHOUSE1, PRODUCT1.getId(), 3));
    }

    @Test
    public void addNewProduct() {
        service.addProduct(WAREHOUSE2.getId(), PRODUCT1.getId(), 1);
        Warehouse actual = dao.get(WAREHOUSE2.getId());
        assertThat(actual).isEqualToComparingFieldByField(addProductExpect(WAREHOUSE2, PRODUCT1.getId(), 1));
    }

    @Test(expected = NotFoundException.class)
    public void addUnknownProduct() {
        service.addProduct(WAREHOUSE2.getId(), 12, 2);
    }

    @Test
    public void reduceProduct() {
        service.reduceProduct(WAREHOUSE3.getId(), PRODUCT1.getId(), 3);
        Warehouse actual = dao.get(WAREHOUSE3.getId());
        assertThat(actual).isEqualToComparingFieldByField(reduceProductExpect(WAREHOUSE3, PRODUCT1.getId(), 3));
    }

    @Test
    public void removeProduct() {
        service.reduceProduct(WAREHOUSE1.getId(), PRODUCT2.getId(), 2);
        Warehouse actual = dao.get(WAREHOUSE1.getId());
        assertThat(actual).isEqualToComparingFieldByField(removeProductExpect(WAREHOUSE1, PRODUCT2.getId()));
    }

    @Test(expected = OutOfStockException.class)
    public void reduceProductOutOfStock() {
        service.reduceProduct(WAREHOUSE2.getId(), PRODUCT4.getId(), 80);
    }


    @Test(expected = NotFoundException.class)
    public void reduceUnknownProduct() {
        service.reduceProduct(WAREHOUSE2.getId(), PRODUCT1.getId(), 3);
    }

    @Test
    public void updateNameAndGet() {
        service.getAll();
        Warehouse updated = getUpdatedName(WAREHOUSE2, "newName");
        service.updateName(updated.getId(), "newName");
        assertThat(service.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

}