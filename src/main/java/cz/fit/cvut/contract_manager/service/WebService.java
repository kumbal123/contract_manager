package cz.fit.cvut.contract_manager.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import cz.fit.cvut.contract_manager.entity.Price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WebService implements Service {

    private WebClient webClient;

    public WebService(final WebClient webClient) {
        this.webClient = webClient;
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.webClient.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    public static WebService getInstance() {
        return WebService.WebServiceHolder.INSTANCE;
    }

    private static class WebServiceHolder {
        private static final WebService INSTANCE = new WebService(new WebClient(BrowserVersion.CHROME));
    }

    private int formatPriceString(final String str) {
        return Integer.parseInt(str.split("Kč")[0].replaceAll("[^0-9]",""));
    }

    public List<Price> getPrices(final String productName) {
        List<Price> prices = new ArrayList<>();

        try {
            String webLink = "https://www.google.com/search?q=";
            String link = webLink + productName.replaceAll(" ", "+");
            System.out.println(link);
            HtmlPage page = webClient.getPage(link);

            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();

            List<HtmlDivision> anchors = page.getByXPath("//div[contains(@class, 'rwVHAc')]");
            System.out.println("Size: " + anchors.size());



            for (HtmlElement anchor : anchors) {
                HtmlAnchor name = anchor.getFirstByXPath("./div[@role='heading']/a[contains(@class, 'plantl pla-unit-title-link')]");
                HtmlElement priceElem = anchor.getFirstByXPath("./div[contains(@class, 'T4OwTb')]");

                prices.add(new Price(name.getTextContent(), name.getHrefAttribute(), formatPriceString(priceElem.getTextContent())));
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        prices.sort(Comparator.comparing(Price::getPrice));

        return prices;
    }
}
