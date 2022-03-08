package cz.fit.cvut.contract_manager.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String contractId;
    private Date creationDate;
    private Integer lendPrice;
    private Date expireDateOrig;
    private Date expireDateCurr;
    private String itemInfo;
    private String itemSpecification;
    private Integer totalPriceOrig;
    private Integer totalPriceCurr;
    private ContractState state;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<History> history;

    public Contract() {

    }

    public Contract(final String contractId, final Date creationDate, final Integer lendPrice, final Date expireDateOrig,
                    final String itemInfo, final String itemSpecification, final Integer totalPriceOrig,
                    final Customer customer) {
        this.contractId = contractId;
        this.creationDate = creationDate;
        this.lendPrice = lendPrice;
        this.expireDateOrig = expireDateOrig;
        this.expireDateCurr = expireDateOrig;
        this.itemInfo = itemInfo;
        this.itemSpecification = itemSpecification;
        this.totalPriceOrig = totalPriceOrig;
        this.totalPriceCurr = totalPriceOrig;
        this.state = ContractState.VALID;
        this.customer = customer;
        this.history = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Contract contract = (Contract) o;
        return id.equals(contract.id) &&
                expireDateOrig.equals(contract.expireDateOrig) &&
                lendPrice.equals(contract.lendPrice) &&
                creationDate.equals(contract.creationDate) &&
                contractId.equals(contract.contractId) &&
                itemInfo.equals(contract.itemInfo) &&
                itemSpecification.equals(contract.itemSpecification) &&
                totalPriceOrig.equals(contract.totalPriceOrig) &&
                customer.equals(contract.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expireDateOrig, lendPrice, creationDate, contractId, itemInfo, itemSpecification,
                totalPriceOrig, customer);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", toDateOrig='" + expireDateOrig + '\'' +
                ", lendPrice='" + lendPrice + '\'' +
                ", fromDate='" + creationDate + '\'' +
                ", contractId='" + contractId + '\'' +
                ", itemInfo='" + itemInfo + '\'' +
                ", itemSpecification='" + itemSpecification + '\'' +
                ", totalPrice='" + totalPriceOrig + '\'' +
                ", customer=" + customer +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public Date getExpireDateOrig() {
        return expireDateOrig;
    }

    public Date getExpireDateCurr() {
        return expireDateCurr;
    }

    public Integer getLendPrice() {
        return lendPrice;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getContractId() {
        return contractId;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public String getItemSpecification() {
        return itemSpecification;
    }

    public Integer getTotalPriceOrig() {
        return totalPriceOrig;
    }

    public Integer getTotalPriceCurr() {
        return totalPriceCurr;
    }

    public ContractState getState() {
        return state;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getName() {
        return customer.getName();
    }

    public String getGender() {
        return customer.getGender();
    }

    public String getAddress() {
        return customer.getAddress();
    }

    public String getCity() {
        return customer.getCity();
    }

    public String getPersonalNumber() {
        return customer.getPersonalNumber();
    }

    public String getIdNumber() {
        return customer.getCardIdNumber();
    }

    public String getMeu() {
        return customer.getMeu();
    }

    public String getNationality() {
        return customer.getNationality();
    }

    public Date getDateOfBirth() {
        return customer.getDateOfBirth();
    }

    public Set<History> getHistory() {
        return history;
    }

    public int getNumberOfProlongs() {
        return history.size();
    }

    public Boolean isValid() {
        return state == ContractState.VALID;
    }

    public Boolean isExpired() {
        return state == ContractState.EXPIRED;
    }

    public Boolean isWithdrawn() {
        return state == ContractState.WITHDRAWN;
    }

    public Boolean isTakenOut() {
        return state == ContractState.TAKEN_OUT;
    }

    public void setExpireDateOrig(final Date date) {
        this.expireDateOrig = date;
    }

    public void setLendPrice(final Integer lendPrice) {
        this.lendPrice = lendPrice;
    }

    public void setCreationDate(final Date date) {
        this.creationDate = date;
    }

    public void setContractId(final String contractId) {
        this.contractId = contractId;
    }

    public void setItemInfo(final String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public void setItemSpecification(final String itemSpecification) {
        this.itemSpecification = itemSpecification;
    }

    public void setTotalPriceOrig(final Integer totalPrice) {
        this.totalPriceOrig = totalPrice;
    }

    public void setState(final ContractState state) {
        this.state = state;
    }

    public void removeCustomer() {
        customer = null;
    }

    public void addHistory(final History history) {
        this.history.add(history);
        this.expireDateCurr = history.getToDate();
        this.totalPriceCurr = history.getTotalPrice();
        this.state = ContractState.VALID;
    }
}
