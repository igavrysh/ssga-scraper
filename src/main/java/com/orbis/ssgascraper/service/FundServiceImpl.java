package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.repository.FundRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FundServiceImpl implements FundService {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(FundServiceImpl.class));

    private final FundRepo fundRepo;

    @Autowired
    public FundServiceImpl(FundRepo fundRepo) {
        this.fundRepo = fundRepo;
    }

    /**
     * This function update the fund if found else adds new event in the collection.
     *
     * @param data: Takes fund object to add in the collection.
     */
    public void upsert(Object data) {
        Fund fund = (Fund) data;;


        /*
        // find the event by title which will be unique
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.EVENTS_TITLE).is(event.getTitle()));

        // prepare update object
        Update update = new Update();
        update.set(Constants.EVENTS_TITLE, event.getTitle());
        update.set(Constants.EVENTS_WEBSITE, event.getWebsite());
        update.set(Constants.EVENTS_START_DATE, event.getStartDate());
        update.set(Constants.EVENTS_END_DATE, event.getEndDate());
        update.set(Constants.EVENTS_LOCATION, event.getLocation());

        // execute the query
        mongoTemplate.upsert(query, update, Events.class);
         */
    }
}
