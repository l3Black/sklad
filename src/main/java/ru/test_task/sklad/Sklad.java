package ru.test_task.sklad;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import ru.test_task.sklad.model.Product;
import ru.test_task.sklad.model.Store;
import ru.test_task.sklad.model.document.DocType;
import ru.test_task.sklad.model.document.Document;
import ru.test_task.sklad.service.DocService;
import ru.test_task.sklad.service.ProductService;
import ru.test_task.sklad.service.StoreService;
import ru.test_task.sklad.to.DocumentTo;
import ru.test_task.sklad.to.ProductTo;
import ru.test_task.sklad.to.Status;
import ru.test_task.sklad.util.exceptions.NotFoundException;
import ru.test_task.sklad.util.exceptions.OutOfStockException;
import ru.test_task.sklad.util.exceptions.ProductArgumentException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.test_task.sklad.to.Response.jsonResponse;
import static ru.test_task.sklad.util.SqlUtil.initDb;
import static ru.test_task.sklad.util.SqlUtil.populateDb;
import static ru.test_task.sklad.util.ValidationUtil.assureIdConsistent;
import static ru.test_task.sklad.util.ValidationUtil.checkValidProductAttributes;
import static spark.Spark.*;

public class Sklad {
    public static final EntityManagerFactory factory;
    public static final Properties properties = new Properties();
    private static final String jsonType = "application/json";
    private static final Logger log = getLogger(Sklad.class);

    private static final ProductService productService;
    private static final StoreService storeService;
    private static final DocService docService;

    static {
        try {
            properties.load(Objects.requireNonNull(Sklad.class.getClassLoader().getResourceAsStream("db/db.config.properties")));
            Boolean isInitDb = Boolean.valueOf(properties.getProperty("initDB"));
            if (isInitDb) {
                initDb();
            }
            Boolean isPopulateDb = Boolean.valueOf(properties.getProperty("populateDB"));
            if (isPopulateDb) {
                populateDb();
            }
        } catch (IOException e) {
            log.error("Failed to load properties", e);
        }

        factory = Persistence.createEntityManagerFactory("ru.test_task.sklad");
        productService = new ProductService();
        storeService = new StoreService();
        docService = new DocService();
    }

    public static void main(String[] args) {

        if (args.length == 1) {
            try {
                int port = Integer.parseInt(args[0]);
                if (port < 1 || port > 65535) throw new NumberFormatException();
                port(port);
            } catch (NumberFormatException e) {
                log.error("The port must be an integer range from 1 to 65535. " +
                        "The server is running on port 4567", e);
            }
        }

        path("/sklad", () -> {
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
                    checkValidProductAttributes(toUpdate);
                    assureIdConsistent(toUpdate, Long.parseLong(req.params(":id")));
                    productService.update(toUpdate);
                    return jsonResponse(Status.SUCCESS, "updated: " + toUpdate);
                });
                post("", (req, res) -> {
                    res.type(jsonType);
                    res.status(201);
                    Product product = new Gson().fromJson(req.body(), Product.class);
                    checkValidProductAttributes(product);
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

            //store controllers
            path("/stores", () -> {
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
                    return jsonResponse(Status.SUCCESS, "store deleted");
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
                            + req.params(":name") + " for storeId= "
                            + req.params(":id"));
                });
                put("/:id/add/:prodId/:amount", (req, res) -> {
                    res.type(jsonType);
                    res.status(202);
                    long id = Long.parseLong(req.params(":id"));
                    long prodId = Long.parseLong(req.params(":prodId"));
                    int quantity = Integer.parseInt(req.params(":amount"));
                    storeService.addProducts(id, prodId, quantity);
                    return jsonResponse(Status.SUCCESS, quantity + " products (id= "
                            + prodId + ") were delivered to the store (id= " + id + ")");
                });
                put("/:id/reduce/:prodId/:amount", (req, res) -> {
                    res.type(jsonType);
                    res.status(202);
                    long id = Long.parseLong(req.params(":id"));
                    long prodId = Long.parseLong(req.params(":prodId"));
                    int quantity = Integer.parseInt(req.params(":amount"));
                    storeService.reduceProducts(id, prodId, quantity);
                    return jsonResponse(Status.SUCCESS, quantity + " products (id= "
                            + prodId + ") removed from store (id= " + id + ")");
                });
            });
            //======================================================================

            //document controllers
            path("/documents", () -> {
                post("/import", (req, res) -> {
                    res.type(jsonType);
                    res.status(201);
                    Document document = new Gson().fromJson(req.body(), Document.class);
                    DocumentTo applied = new DocumentTo(docService.apply(document));
                    return jsonResponse(applied, Status.SUCCESS);
                });
                get("/:id", (req, res) -> {
                    res.type(jsonType);
                    res.status(302);
                    DocumentTo document = new DocumentTo(docService.get(Long.parseLong(req.params(":id"))));
                    return jsonResponse(document, Status.SUCCESS);
                });
                get("", (req, res) -> {
                    res.type(jsonType);
                    res.status(302);
                    Collection<DocumentTo> documents = docService.getAll().stream()
                            .map(DocumentTo::new)
                            .collect(Collectors.toList());
                    return jsonResponse(documents, Status.SUCCESS);
                });
                get("/type/:type", (req, res) -> {
                    res.type(jsonType);
                    res.status(302);
                    DocType type = DocType.valueOf(req.params(":type").toUpperCase());
                    Collection<DocumentTo> documents = docService.getByType(type).stream()
                            .map(DocumentTo::new)
                            .collect(Collectors.toList());
                    return jsonResponse(documents, Status.SUCCESS);
                });
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
