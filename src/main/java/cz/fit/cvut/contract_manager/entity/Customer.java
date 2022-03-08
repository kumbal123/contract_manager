package cz.fit.cvut.contract_manager.entity;

import cz.fit.cvut.contract_manager.util.Util;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String gender;
    private String address;
    private String city;
    private String personalNumber;
    private String cardIdNumber;
    private String meu;
    private String nationality;
    private Date dateOfBirth;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts;

    public Customer() {

    }

    public Customer(final String name, final String gender, final String address, final String city,
                    final String personalNumber, final String cardIdNumber, final String meu,
                    final String nationality, final Date dateOfBirth) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.personalNumber = personalNumber;
        this.cardIdNumber = cardIdNumber;
        this.meu = meu;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.contracts = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getCardIdNumber() {
        return cardIdNumber;
    }

    public String getMeu() {
        return meu;
    }

    public String getNationality() {
        return nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public int getNumberOfContracts() {
        return contracts.size();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setPersonalNumber(final String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public void setCardIdNumber(final String cardIdNumber) {
        this.cardIdNumber = cardIdNumber;
    }

    public void setMeu(final String meu) {
        this.meu = meu;
    }

    public void setNationality(final String nationality) {
        this.nationality = nationality;
    }

    public void setDateOfBirth(final Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void assignContract(Contract contract) {
        this.contracts.add(contract);
    }

    public int getContractCount(int month, int year) {
        int count = 0;

        for(Contract contract : contracts) {
            int contractMonth = Util.getMonth(contract.getCreationDate());
            int contractYear = Util.getYear(contract.getCreationDate());

            count += contractMonth == month && contractYear == year ? 1 : 0;
        }

        return count;
    }

    public int getContractCount(int year) {
        int count = 0;

        for(Contract contract : contracts) {
            count += Util.getYear(contract.getCreationDate()) == year ? 1 : 0;
        }

        return count;
    }

    public int getMoneySpent(int month, int year) {
        int money = 0;

        for(Contract contract : contracts) {
            int contractMonth = Util.getMonth(contract.getCreationDate());
            int contractYear = Util.getYear(contract.getCreationDate());
            int interest = contract.getTotalPriceCurr() - contract.getLendPrice();

            money += contractMonth == month && contractYear == year && contract.isWithdrawn() ? interest : 0;
        }

        return money;
    }

    public int getMoneySpent(int year) {
        int money = 0;

        for(Contract contract : contracts) {
            int interest = contract.getTotalPriceCurr() - contract.getLendPrice();
            money += Util.getYear(contract.getCreationDate()) == year && contract.isWithdrawn() ? interest : 0;
        }

        return money;
    }
}
