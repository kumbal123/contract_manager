package cz.fit.cvut.contract_manager.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contract {
    @Id
    private String id;
    private Integer price;

    //private Customer;

    public Contract() {

    }

    public Contract(String id, Integer price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
