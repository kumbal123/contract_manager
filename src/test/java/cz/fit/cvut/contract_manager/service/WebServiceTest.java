package cz.fit.cvut.contract_manager.service;

import cz.fit.cvut.contract_manager.entity.Price;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebServiceTest {

    private WebService webService = WebService.getInstance();

    @Test
    void shouldGetPricesFromHtmlPage() {
        webService.setPage("file:src/main/resources/html/data.html");

        List<Price> prices = webService.getPrices("", null);

        int total = 0;

        for(Price price : prices) {
            total += price.getPrice();
        }

        assertEquals(1295270, total);
        assertEquals(25, prices.size());
        assertEquals(51810, total / prices.size());
    }
}