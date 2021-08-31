package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.exception.DataModelIncorrectStateException;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import com.orbis.ssgascraper.repository.FundRepo;
import com.orbis.ssgascraper.transformer.FundDtoTransformer;
import com.orbis.ssgascraper.transformer.FundTransformer;
import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class FundService {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(FundService.class));

    private final FundRepo fundRepo;

    /**
     * This function update the fund if found else adds new fund in the collection.
     *
     * @param data: Takes fund object to add in the collection.
     */
    public Fund upsert(Object data) {
        FundDto fundDto = (FundDto) data;
        if (fundDto.getTicker() == null) {
            return null;
        }

        Fund fundPersisted = fundWithFundDto(fundDto);
        if (fundPersisted == null) {
            Fund fund = FundDtoTransformer.toModel(fundDto);
            fund.setCreated(LocalDateTime.now());
            fundRepo.saveAndFlush(fund);
        } else {
            FundTransformer.updateFundAtomicFieldsWithDto(fundPersisted, fundDto);
            fundPersisted.setModified(LocalDateTime.now());
            fundRepo.saveAndFlush(fundPersisted);
        }

        return fundWithFundDto(fundDto);
    }

    public Fund fundWithFundDto(FundDto dto) {
        String ticker = dto.getTicker();
        List<Fund> fundsResult = fundRepo.findByTickerEquals(ticker);
        if (fundsResult.size() > 1) {
            throw new DataModelIncorrectStateException(
                String.format("Fund Repo more than one (%s) records for ticker %s",
                    fundsResult.size(), ticker));
        }
        return fundsResult.size() > 0 ? fundsResult.get(0) : null;
    }

}
