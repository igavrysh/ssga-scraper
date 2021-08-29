package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.exception.DataModelIncorrectStateException;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.repository.FundRepo;
import com.orbis.ssgascraper.util.FundMapper;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class FundService {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(FundService.class));

    private final FundRepo fundRepo;

    /**
     * This function update the fund if found else adds new event in the collection.
     *
     * @param data: Takes fund object to add in the collection.
     */
    public void upsert(Object data) {
        FundDto fundDto = (FundDto) data;

        if (fundDto.getTicker() == null) {
            return;
        }

        List<Fund> funds = fundRepo.findByTickerEquals(fundDto.getTicker());
        if (funds.size() == 0) {
            Fund fund = FundMapper.fundFromDto(fundDto);
            fund.setCreated(LocalDateTime.now());
            fundRepo.save(fund);
        } else if (funds.size() == 1) {
            Fund fundPersisted = funds.get(0);
            ///FundMapper.updateFundFromDto(fundDto, fundPersisted);
            fundPersisted.setModified(LocalDateTime.now());
            fundRepo.save(fundPersisted);
        } else {
            throw new DataModelIncorrectStateException(
                    String.format("Fund Repo more than one (%s) records for ticker %s", funds.size(), fundDto.getTicker()));
        }
    }
}
