package com.orbis.ssgascraper.webscraper;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
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
                Element nameEl = row.select("tr:nth-of-type(1) td.fundName a").first();
                Element tickerEl = row.select("tr:nth-of-type(1) td.fundTicker a").first();
                Element domicileEl = row.select("tr:nth-of-type(1) td.domicile div").first();

                if (nameEl != null && tickerEl != null && domicileEl != null) {
                    Fund fund = Fund.builder()
                            .name(nameEl.text())
                            .ticker(tickerEl.text())
                            .domicile(domicileEl.text())
                            .link(nameEl.attr("href"))
                            .build();
                    scraperFundDataState.getDataQueue().add(fund);
                } else {
                    LOGGER.log(Level.SEVERE,
                            String.format("SsgaScrapper failed to parse funds, name %s, ticker %s, domicile $s", nameEl, tickerEl, domicileEl));
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
        JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.UTC)
                .maxConnections(200)
                .socketTimeout(10000)
                .connectionReqTimeout(10000)
                .connectTimeout(10000).build());
        try {
            LOGGER.log(Level.INFO, "Driver creating successful");
            driver.get(ScraperInfo.SSGA.URL);
            String loadedPage = driver.getPageSource();
            LOGGER.log(Level.INFO, "STARTED JSOUP PARSING");
            final Document document = Jsoup.parse(loadedPage);

            // process document

            processDocument(document);

            /*
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
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
             */

        } catch (Exception ex) {

            // release the ScraperDataDispatcher thread if exception occurs.
            scraperFundDataState.setIsActive(false);
            ex.printStackTrace();
        } finally{
            driver.quit();
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
