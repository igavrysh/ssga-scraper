package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FundRepo extends JpaRepository<Fund, UUID> {

    List<Fund> findByTickerEquals(@Param("ticker") String ticker);

    Optional<Fund> findByName(@Param("name") String name);

}
