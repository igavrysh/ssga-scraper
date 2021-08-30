package com.orbis.ssgascraper.repository;

import com.orbis.ssgascraper.model.Weight;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRepo extends JpaRepository<Weight, UUID> {

}
