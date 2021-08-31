package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.enums.WeightType;
import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.model.Weight;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface WeightRepo extends JpaRepository<Weight, UUID> {

  List<Weight> findByNameAndDateAndTypeAndFund(
      @Param("name") String name, @Param("date") LocalDate date, @Param("type") WeightType type,
      @Param("fund") Fund fund);

}
