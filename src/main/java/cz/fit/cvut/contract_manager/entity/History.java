package cz.fit.cvut.contract_manager.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.max;

@Entity
public class History implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer startPrice;
    private Date fromDate;
    private Date toDate;
    private Integer interest;
    private Integer totalPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    private Contract contract;

    public static Integer computeInterest(final Integer price, final Date fromDate, final Date toDate) {
        int days = (int) TimeUnit.DAYS.convert(toDate.getTime() - fromDate.getTime(), TimeUnit.MILLISECONDS);
        return max(days * price / 100, 20);
    }

    public History() {

    }

    public History(final Integer startPrice, final Date fromDate, final Date toDate, final Contract contract) {
        this.startPrice = startPrice;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.interest = computeInterest(startPrice, fromDate, toDate);
        this.totalPrice = startPrice + interest;
        this.contract = contract;
    }

    public History(final Integer startPrice, final Date fromDate, final Date toDate,
                   final Integer interest, final Integer totalPrice, final Contract contract) {
        this.startPrice = startPrice;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.interest = interest;
        this.totalPrice = totalPrice;
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }

        if(o == null || getClass() != o.getClass()){
            return false;
        }

        History history = (History) o;
        return id.equals(history.id) &&
                startPrice.equals(history.startPrice) &&
                interest.equals(history.interest) &&
                totalPrice.equals(history.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startPrice, interest, totalPrice, fromDate, toDate, contract);
    }

    public Integer getStartPrice() {
        return startPrice;
    }

    public Integer getInterest() {
        return interest;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Contract getContract() {
        return contract;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setStartPrice(final Integer startPrice) {
        this.startPrice = startPrice;
    }

    public void setInterest(final Integer interest) {
        this.interest = interest;
    }

    public void setTotalPrice(final Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setContract(final Contract contract) {
        this.contract = contract;
    }
}
