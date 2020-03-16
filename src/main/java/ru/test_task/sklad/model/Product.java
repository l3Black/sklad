package ru.test_task.sklad.model;

import javax.persistence.*;

@NamedQueries({@NamedQuery(name = Product.DELETE, query = "DELETE FROM Product p WHERE p.id=:id"),
        @NamedQuery(name = Product.GET_ALL, query = "SELECT p FROM Product p ORDER BY p.id"),
        @NamedQuery(name = Product.GET, query = "SELECT p FROM Product p WHERE p.id=:id"),
        @NamedQuery(name = Product.GET_ALL_BY_NAME, query = "SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :name,'%'))")
})

@Entity
@Table(name = "product")
public class Product extends AbstractBaseEntity {
    public static final String DELETE = "Product.delete";
    public static final String GET_ALL = "Product.getAll";
    public static final String GET = "Product.get";
    public static final String GET_ALL_BY_NAME = "Product.getByName";

    @Column(name = "name")
    private String name;

    @Column(name = "purchasePrice")
    private Float purchasePrice;

    @Column(name = "sellingPrice")
    private Float sellingPrice;

    public Product() {
    }

    public Product(Long id, String name, Float purchasePrice, Float sellingPrice) {
        super(id);
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", sellingPrice=" + sellingPrice +
                '}';
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
