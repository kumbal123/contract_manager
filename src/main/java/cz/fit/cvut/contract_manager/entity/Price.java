package cz.fit.cvut.contract_manager.entity;

public class Price {
    private String name;
    private String link;
    private Integer price;

    public Price() {

    }

    public Price(final String name, final String link, final Integer price) {
        this.name = name;
        this.link = link;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Integer getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
