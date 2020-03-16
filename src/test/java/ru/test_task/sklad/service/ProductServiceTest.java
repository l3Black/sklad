package ru.test_task.sklad.service;

import org.junit.Assert;
import org.junit.Test;
import ru.test_task.sklad.dao.ProductDao;
import ru.test_task.sklad.dao.ProductDaoJpa;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.util.exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.test_task.sklad.ProductTestData.*;

public class ProductServiceTest extends AbstractServiceTest {

    private ProductService service = new ProductService();
    private ProductDao dao = new ProductDaoJpa();

    @Test
    public void create() {
        Product newProduct = getNewProduct();
        Product actual = service.create(newProduct);
        newProduct.setId(actual.getId());
        assertThat(actual).isEqualToComparingFieldByField(newProduct);
    }

    @Test
    public void getAll() {
        Collection<Product> actual = service.getAll();
        Collection<Product> expected = getAllExpected();
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void get() {
        Product actual = service.get(PRODUCT1.getId());
        assertThat(actual).isEqualToComparingFieldByField(PRODUCT1);
    }

    @Test
    public void getAllByName() {
        Collection<Product> actual = service.getAll(PRODUCT1.getName());
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(List.of(PRODUCT1));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(1);
    }

    @Test
    public void update() {
        Product updated = getUpdatedProduct();
        service.update(updated);
        assertThat(dao.get(updated.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void delete() {
        service.delete(PRODUCT1.getId());
        Assert.assertNull(dao.get(PRODUCT1.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(1);
    }
}