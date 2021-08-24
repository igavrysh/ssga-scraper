package com.orbis.ssgascraper.webscraper;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.service.ServiceProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class scrapes the data from the website and add in the queue.
 * It will also run on the separate thread other then the main thread.
 */
@Component
public class SsgaScraper implements Scraper, Runnable {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(SsgaScraper.class));

    private final ScraperDataState<Fund> scraperFundDataState;

    @Autowired
    public SsgaScraper(ScraperStateManager<Fund> scraperStateManager,
                       ScraperDataState<Fund> scraperFundDataState,
                       ServiceProvider serviceProvider) {
        this.scraperFundDataState = scraperFundDataState;

        // initialize the the state in scraperStateHolder
        scraperFundDataState.setIsActive(true);
        scraperFundDataState.setScraperId(ScraperInfo.SSGA.ID);

        // pass the database operation from which data service needs to get executed
        // lambda fn is needed because right now I dont know on which index our state
        // is getting registered by scraperStateManager. So manage iterate the list and
        // call the function with appropriate data
        scraperFundDataState.setConsumer(t -> serviceProvider.getFundService().upsert(t));

        // register the the state in scraperStateHolder
        scraperStateManager.registerScraperState(scraperFundDataState);
    }

    /**
     * Processes and extracts the document using CSS query selectors
     *
     * @param document: HTML document
     */
    private void processDocument(Document document) {
        LOGGER.log(Level.INFO, "Started Processing SsgaScraper document");

        // make this active as we are continuously scraping
        scraperFundDataState.setIsActive(true);

        try {
            // iterate over the document
            for (Element row : document.select("div.tb-body table tbody ")) {

                // query selectors to scrape the data.
                Element nameEl = row.select("tr:nth-of-type(1) td.fundName").first();
                Element tickerEl = row.select("tr:nth-of-type(1) td.fundTicker").first();
                Element domicileEl = row.select("tr:nth-of-type(1) td.domicile").first();

                if (nameEl != null && tickerEl != null && domicileEl != null) {
                    Fund fund = Fund.builder()
                            .name(nameEl.text())
                            .ticker(tickerEl.text())
                            .domicile(domicileEl.text())
                            .build();
                    scraperFundDataState.getDataQueue().add(fund);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error occurred while processing the document");
        }

        // reset the status to false in order to release the ScraperDataDispatcher thread
        // which is used to insert data in the database.
        scraperFundDataState.setIsActive(false);
        LOGGER.log(Level.INFO, "Finished Processing SsgaScraper document.");
    }

    @Override
    public void startScraper() {
        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            // Wait time
            webClient.waitForBackgroundJavaScript(15000);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            URL url = new URL(ScraperInfo.SSGA.URL);
            WebRequest requestSettings = new WebRequest(url, HttpMethod.GET);
            HtmlPage page = webClient.getPage(requestSettings);
            synchronized (page) {
                try {
                    page.wait(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Jsoup is used to parse the HTML.
            final Document document = Jsoup.parse(page.asXml());

            // process document
            processDocument(document);
        } catch (Exception ex) {

            // release the ScraperDataDispatcher thread if exception occurs.
            scraperFundDataState.setIsActive(false);
            ex.printStackTrace();
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOGGER.log(Level.INFO,
                String.format("[**Scraper SsgaScrapper Thread**]: %s", Thread.currentThread().getName()));
        startScraper();
    }
}
