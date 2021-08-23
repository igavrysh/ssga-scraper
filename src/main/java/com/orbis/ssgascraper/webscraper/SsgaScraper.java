package com.orbis.ssgascraper.webscraper;

import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.service.ServiceProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * Processes and extract the document using CSS query selectors
     *
     * @param document: HTML document
     */
    private void processDocument(Document document) {
    }

    @Override
    public void startScraper() {
        try {
            // Jsoup is used to parse the HTML.
            final Document document = Jsoup.connect(ScraperInfo.SSGA.URL).get();

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
