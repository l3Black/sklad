package ru.test_task.sklad.model.document;

import com.google.gson.Gson;
import ru.test_task.sklad.model.AbstractBaseEntity;

import javax.persistence.*;
import java.util.Map;

@NamedQueries({@NamedQuery(name = DocEntity.GET, query = "SELECT d FROM DocEntity d WHERE d.id=:id"),
        @NamedQuery(name = DocEntity.GET_ALL, query = "SELECT d FROM DocEntity d"),
        @NamedQuery(name = DocEntity.GET_BY_TYPE, query = "SELECT d FROM DocEntity d WHERE d.type=:type")
})

@Entity
@Table(name = "doc_entity")
public class DocEntity extends AbstractBaseEntity {
    public static final String GET = "Doc.get";
    public static final String GET_ALL = "Doc.getAll";
    public static final String GET_BY_TYPE = "Doc.getByType";

    @Enumerated(EnumType.STRING)
    private DocType type;

    @Column(name = "enter_store_id")
    private Long enterStoreId;

    @Column(name = "write_off_store_id")
    private Long writeOffStoreId;
    private String products;

    public DocEntity() {
    }

    public DocEntity(Document document) {
        super(null);
        this.type = document.getType();
        this.enterStoreId = document.getEnterStoreId();
        this.writeOffStoreId = document.getWriteOffStoreId();
        this.products = new Gson().toJson(document.getProducts());
    }

    public DocEntity(Long id, DocType type, Long enterStoreId, Long writeOffStoreId, Map<Long, Integer> products) {
        super(id);
        this.type = type;
        this.enterStoreId = enterStoreId;
        this.writeOffStoreId = writeOffStoreId;
        this.products = new Gson().toJson(products);
    }

    @Override
    public String toString() {
        return "DocEntity{" +
                "id=" + id +
                ", type=" + type +
                ", entranceWarehouseId=" + enterStoreId +
                ", writeOffWarehouseId=" + writeOffStoreId +
                ", products='" + products + '\'' +
                '}';
    }

    //getter and setters
    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getProducts() {
        return new Gson().fromJson(products, Map.class);
    }


    public DocType getType() {
        return type;
    }

    public void setType(DocType type) {
        this.type = type;
    }

    public Long getEnterStoreId() {
        return enterStoreId;
    }

    public void setEnterStoreId(Long entranceWarehouseId) {
        this.enterStoreId = entranceWarehouseId;
    }

    public Long getWriteOffStoreId() {
        return writeOffStoreId;
    }

    public void setWriteOffStoreId(Long writeOffWarehouseId) {
        this.writeOffStoreId = writeOffWarehouseId;
    }
}
