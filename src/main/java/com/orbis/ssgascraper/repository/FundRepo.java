package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundRepo extends JpaRepository<Fund, Long> {

    Optional<Fund> findByTicker(@Param("ticker") String ticker);

    Optional<Fund> findByName(@Param("name") String name);
}
