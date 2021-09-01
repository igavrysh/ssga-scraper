package com.orbis.ssgascraper.service;

import com.orbis.ssgascraper.model.Fund;
import com.orbis.ssgascraper.repository.FundRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FundsService {

    private final FundRepo fundRepo;

    public List<Fund> getFunds() {
        return fundRepo.findAll();
    }

}
