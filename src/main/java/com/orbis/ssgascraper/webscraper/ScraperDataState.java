package com.orbis.ssgascraper.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Class maintains the state of the Scraper Data for any type of collection.
 */
@Component
@Getter
@Setter
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScraperDataState<T> {
    private Boolean isActive;
    private Queue<T> dataQueue;
    private Integer scraperId;
    private Consumer<T> consumer;

    public ScraperDataState() {
        this.dataQueue = new LinkedList<>();
    }
}
