package ru.test_task.sklad.model;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@NamedQueries({@NamedQuery(name = Warehouse.GET_ALL, query = "SELECT DISTINCT w FROM Warehouse w LEFT JOIN FETCH w.products ORDER BY w.id"),
        @NamedQuery(name = Warehouse.DELETE, query = "DELETE FROM Warehouse w WHERE w.id=:id"),
        @NamedQuery(name = Warehouse.GET, query = "SELECT w FROM Warehouse w WHERE w.id=:id"),
        @NamedQuery(name = Warehouse.UPDATE_NAME, query = "UPDATE Warehouse w SET w.name=:name WHERE w.id=:id")
})

@Entity
@Table(name = "warehouse")
@Access(AccessType.FIELD)
public class Warehouse extends AbstractBaseEntity {
    public static final String GET_ALL = "Warehouse.getAll";
    public static final String DELETE = "Warehouse.delete";
    public static final String GET = "Warehouse.get";
    public static final String UPDATE_NAME = "Warehouse.updateName";

    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "products_in_warehouse",
            joinColumns = {@JoinColumn(name = "warehouse_id", referencedColumnName = "id")})
    @Column(name = "product_quantity", nullable = false)
    @MapKeyColumn(name = "product_id")
    private Map<Long, Integer> products = new LinkedHashMap<>();

    public Warehouse() {
    }

    public Warehouse(Warehouse w) {
        super(w.getId());
        this.name = w.getName();
        this.products = new LinkedHashMap<>(w.getProducts());
    }

    public Warehouse(Long id, String name, Map<Long, Integer> products) {
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
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
