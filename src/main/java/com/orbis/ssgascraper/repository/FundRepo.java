package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundRepo extends JpaRepository<Fund, Long> {

    Optional<Fund> findByTicker(@Param("ticker") String ticker);

    Optional<Fund> findByName(@Param("name") String name);

    @Modifying
    @Query("update Fund f set f.name = :name where f.id = :id")
    void updateName(@Param(value = "id") long id, @Param(value = "name") String name);

    @Modifying
    @Query("update Fund f set f.description = :description where f.id = :id")
    void updateDescription(@Param(value = "id") long id, @Param(value = "description") String description);

    @Modifying
    @Query("update Fund f set f.domicile = :domicile where f.id = :id")
    void updateDomicile(@Param(value = "id") long id, @Param(value = "domicile") String domicile);

    @Modifying
    @Query("update Fund f set f.link = :link where f.id = :id")
    void updateLink(@Param(value = "id") long id, @Param(value = "link") String link);
}
