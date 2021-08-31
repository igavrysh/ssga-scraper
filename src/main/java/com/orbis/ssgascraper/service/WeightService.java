package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.dto.WeightDto;
import com.orbis.ssgascraper.exception.DataModelIncorrectStateException;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import com.orbis.ssgascraper.repository.FundRepo;
import com.orbis.ssgascraper.repository.WeightRepo;
import com.orbis.ssgascraper.transformer.WeightDtoTransformer;
import com.orbis.ssgascraper.transformer.WeightTransformer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeightService {

  private final static Logger LOGGER = Logger.getLogger(String.valueOf(WeightService.class));

  private final WeightRepo weightRepo;

  /**
   * This function update the Weight if found else adds new weight in the collection.
   *
   */
  public Weight upsert(WeightDto weightDto, Fund fund) {
    Weight weightPersisted = weightWithDtoAndFund(weightDto, fund);
    if (weightPersisted == null) {
      Weight weight = WeightDtoTransformer.toModel(weightDto);
      weight.setCreated(LocalDateTime.now());
      weight.setFund(fund);
      weightRepo.saveAndFlush(weight);
    } else {
      WeightTransformer.updateWeightAtomicFieldsWithDto(weightPersisted, weightDto);
      weightPersisted.setModified(LocalDateTime.now());
      weightRepo.saveAndFlush(weightPersisted);
    }
    return weightWithDtoAndFund(weightDto, fund);
  }

  private Weight weightWithDtoAndFund(WeightDto weightDto, Fund fund) {

    if (fund == null) {
      throw new DataModelIncorrectStateException(
          String.format("Weight insert failed, no fund is present with ticker %s",
              fund.getTicker()));
    }

    List<Weight> weightsResult = weightRepo.findByNameAndDateAndTypeAndFund(
        weightDto.getName(), weightDto.getDate(), weightDto.getType(), fund);
    if (weightsResult.size() > 1) {
      throw new DataModelIncorrectStateException(
          String.format("Weight Repo more than one (%s) records for name %s, fund ticker %s",
              weightsResult.size(), weightDto.getName(), fund.getName()));
    }
    return weightsResult.size() > 0 ? weightsResult.get(0) : null;
  }

}
