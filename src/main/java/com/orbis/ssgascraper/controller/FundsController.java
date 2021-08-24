package com.orbis.ssgascraper.controller;

import com.orbis.ssgascraper.constants.Routes;
import com.orbis.ssgascraper.dto.FundDTO;
import com.orbis.ssgascraper.service.FundService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Supports GET operation", tags = {"Fund"})
@RestController
@RequestMapping(Routes.FUNDS_API)
public class FundsController {

    private FundService fundService;

    @Autowired
    public FundsController(FundService fundService) {
        this.fundService = fundService;
    }

    @GetMapping
    public ResponseEntity<?> getFunds() {
        List<FundDTO> result = new ArrayList<>();
        return ResponseEntity.ok(result);
    }
}
