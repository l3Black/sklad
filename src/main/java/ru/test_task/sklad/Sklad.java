package ru.test_task.sklad;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.model.document.DocEntity;
import ru.test_task.sklad.model.document.DocType;
import ru.test_task.sklad.model.document.Document;
import ru.test_task.sklad.service.DocService;
import ru.test_task.sklad.service.ProductService;
import ru.test_task.sklad.service.StoreService;
import ru.test_task.sklad.to.ProductTo;
import ru.test_task.sklad.to.Status;
import ru.test_task.sklad.util.ValidationUtil;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;
import ru.test_task.sklad.util.exceptions.ProductArgumentException;

import java.util.Collection;

import static ru.test_task.sklad.to.Response.jsonResponse;
import static spark.Spark.*;

public class Sklad {
    private static String jsonType = "application/json";

    private static ProductService productService = new ProductService();
    private static StoreService storeService = new StoreService();
    private static DocService docService = new DocService();

    public static void main(String[] args) {
        //product controllers
        path("/products", () -> {
            get("", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<Product> products = productService.getAll();
                return jsonResponse(products, Status.SUCCESS);
            });
            get("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Product product = productService.get(Long.parseLong(req.params(":id")));
                return jsonResponse(product, Status.SUCCESS);
            });
            put("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                Product toUpdate = new Gson().fromJson(req.body(), Product.class);
                ValidationUtil.checkValidProductAttributes(toUpdate);
                ValidationUtil.assureIdConsistent(toUpdate, Long.parseLong(req.params(":id")));
                productService.update(toUpdate);
                return jsonResponse(Status.SUCCESS, "updated: " + toUpdate);
            });
            post("", (req, res) -> {
                res.type(jsonType);
                res.status(201);
                Product product = new Gson().fromJson(req.body(), Product.class);
                ValidationUtil.checkValidProductAttributes(product);
                Product saved = productService.create(product);
                return jsonResponse(Status.SUCCESS, "saved: " + saved);
            });
            delete("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                productService.delete(Long.parseLong(req.params(":id")));
                return jsonResponse(Status.SUCCESS, "product deleted");
            });
            get("/name/:name", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<Product> products = productService.getAll(req.params(":name"));
                return jsonResponse(products, Status.SUCCESS);
            });
        });
        //===================================================================

        //warehouse controllers
        path("/warehouses", () -> {
            get("/balance", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<ProductTo> productTos = storeService.balance();
                return jsonResponse(productTos, Status.SUCCESS);
            });
            get("/balance/:id", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<ProductTo> productTos = storeService.balance(Long.parseLong(req.params(":id")));
                return jsonResponse(productTos, Status.SUCCESS);
            });
            delete("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                storeService.delete(Long.parseLong(req.params(":id")));
                return jsonResponse(Status.SUCCESS, "warehouse deleted");
            });
            get("", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<Store> stores = storeService.getAll();
                return jsonResponse(stores, Status.SUCCESS);
            });
            post("", (req, res) -> {
                res.type(jsonType);
                res.status(201);
                Store store = new Gson().fromJson(req.body(), Store.class);
                Store saved = storeService.create(store);
                return jsonResponse(Status.SUCCESS, "saved: " + saved);
            });
            get("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Store store = storeService.get(Long.parseLong(req.params(":id")));
                return jsonResponse(store, Status.SUCCESS);
            });
            put("/:id/:name", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                storeService.updateName(Long.parseLong(req.params(":id")), req.params(":name"));
                return jsonResponse(Status.SUCCESS, "updated name:"
                        + req.params(":name") + " for warehouseId= "
                        + req.params(":id"));
            });
            put("/:id/add/:prodId/:quantity", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                long id = Long.parseLong(req.params(":id"));
                long prodId = Long.parseLong(req.params(":prodId"));
                int quantity = Integer.parseInt(req.params(":quantity"));
                storeService.addProducts(id, prodId, quantity);
                return jsonResponse(Status.SUCCESS, quantity + " products (id= "
                        + prodId + ") were delivered to the warehouse (id= " + id + ")");
            });
            put("/:id/reduce/:prodId/:quantity", (req, res) -> {
                res.type(jsonType);
                res.status(202);
                long id = Long.parseLong(req.params(":id"));
                long prodId = Long.parseLong(req.params(":prodId"));
                int quantity = Integer.parseInt(req.params(":quantity"));
                storeService.reduceProducts(id, prodId, quantity);
                return jsonResponse(Status.SUCCESS, quantity + " products (id= "
                        + prodId + ") removed from warehouse (id= " + id + ")");
            });
        });
        //======================================================================

        //document controllers
        path("/documents", () -> {
            post("/import", (req, res) -> {
                res.type(jsonType);
                res.status(201);
                Document document = new Gson().fromJson(req.body(), Document.class);
                DocEntity applied = docService.apply(document);
                return jsonResponse(Status.SUCCESS, "applied " + applied);
            });
            get("/:id", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                DocEntity document = docService.get(Long.parseLong(req.params(":id")));
                return jsonResponse(document, Status.SUCCESS);
            });
            get("", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<DocEntity> documents = docService.getAll();
                return jsonResponse(documents, Status.SUCCESS);
            });
            get("/type/:type", (req, res) -> {
                res.type(jsonType);
                res.status(302);
                Collection<DocEntity> documents = docService.getByType(DocType.valueOf(req.params(":type").toUpperCase()));
                return jsonResponse(documents, Status.SUCCESS);
            });
        });
        //=======================================================================

        //exceptions handling
        notFound((req, res) -> {
            res.type(jsonType);
            res.status(404);
            return jsonResponse(Status.ERROR, "Page not found");
        });
        internalServerError((req, res) -> {
            res.type(jsonType);
            res.status(500);
            return jsonResponse(Status.ERROR, "internalServerError");
        });
        exception(NotFoundException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
        exception(OutOfStockException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
        exception(JsonSyntaxException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
        exception(IllegalArgumentException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
        exception(NumberFormatException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
        exception(ProductArgumentException.class, (exc, req, res) -> {
            res.type(jsonType);
            res.status(400);
            res.body(jsonResponse(Status.ERROR, exc.getMessage()));
        });
    }
}
