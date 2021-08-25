package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.dto.FundDto;
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
        FundDto f = (FundDto) data;

        if (f.getTicker() == null) {
            return;
        }

        Fund fundPersisted = fundRepo.findByTicker(f.getTicker()).orElse(null);
        if (fundPersisted == null) {
            fundRepo.save(FundDto.fundFromFundDto(f));
        } else {
            Long fundId = fundPersisted.getId();
            if (f.getName() != null) {
                fundRepo.updateName(fundId, f.getName());
            }
            if (f.getDescription() != null) {
                fundRepo.updateDescription(fundId, f.getDescription());
            }
            if (f.getDomicile() != null) {
                fundRepo.updateDomicile(fundId, f.getDomicile());
            }
            if (f.getLink() != null) {
                fundRepo.updateLink(fundId, f.getLink());
            }
        }
    }
}
