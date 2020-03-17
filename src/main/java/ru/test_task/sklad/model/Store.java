package ru.test_task.sklad.model;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@NamedQueries({@NamedQuery(name = Store.GET_ALL, query = "SELECT DISTINCT s FROM Store s LEFT JOIN FETCH s.products ORDER BY s.id"),
        @NamedQuery(name = Store.DELETE, query = "DELETE FROM Store s WHERE s.id=:id"),
        @NamedQuery(name = Store.GET, query = "SELECT s FROM Store s WHERE s.id=:id"),
        @NamedQuery(name = Store.UPDATE_NAME, query = "UPDATE Store s SET s.name=:name WHERE s.id=:id")
})

@Entity
@Table(name = "store")
@Access(AccessType.FIELD)
public class Store extends AbstractBaseEntity {
    public static final String GET_ALL = "Store.getAll";
    public static final String DELETE = "Store.delete";
    public static final String GET = "Store.get";
    public static final String UPDATE_NAME = "Store.updateName";

    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "products_in_store",
            joinColumns = {@JoinColumn(name = "store_id", referencedColumnName = "id")})
    @Column(name = "product_quantity", nullable = false)
    @MapKeyColumn(name = "product_id")
    private Map<Long, Integer> products = new LinkedHashMap<>();

    public Store() {
    }

    public Store(Store s) {
        super(s.getId());
        this.name = s.getName();
        this.products = new LinkedHashMap<>(s.getProducts());
    }

    public Store(Long id, String name, Map<Long, Integer> products) {
        super(id);
        this.name = name;
        this.products = products;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    //Overrides
    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
