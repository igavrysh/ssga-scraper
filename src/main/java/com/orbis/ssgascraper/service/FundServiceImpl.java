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
        Fund fund = (Fund) data;

        if (fund.getTicker() == null) {
            return;
        }

        Fund fundPersisted = fundRepo.findByTicker(fund.getTicker()).orElse(null);
        if (fundPersisted == null) {
            fundRepo.save(fund);
        } else {
            Long fundId = fundPersisted.getId();
            if (fund.getName() != null) {
                fundRepo.updateName(fundId, fund.getName());
            }
            if (fund.getDescription() != null) {
                fundRepo.updateDescription(fundId, fund.getDescription());
            }
            if (fund.getDomicile() != null) {
                fundRepo.updateDomicile(fundId, fund.getDomicile());
            }
            if (fund.getLink() != null) {
                fundRepo.updateLink(fundId, fund.getLink());
            }
        }
    }
}
