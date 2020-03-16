package ru.test_task.sklad.util;

import ru.test_task.sklad.model.AbstractBaseEntity;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.ProductArgumentException;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, long id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, long id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, long id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static void checkValidProductAttributes(Product product) {
        if (product.getName() == null || product.getPurchasePrice() == null || product.getSellingPrice() == null) {
            throw new ProductArgumentException("Product with id=" + product.getId() + " should not have null attributes except id");
        }
        if (product.getSellingPrice() <= 0 || product.getPurchasePrice() <= 0) {
            throw new ProductArgumentException("Product with id= " + product.getId() + " should have positive price");
        }
    }
}