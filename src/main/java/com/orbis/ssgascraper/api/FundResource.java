package com.orbis.ssgascraper.api;

import com.orbis.ssgascraper.dto.FundDetailsDto;
import com.orbis.ssgascraper.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundResource {

    private final FundService fundService;

    @GetMapping("/fund/{ticker}")
    public ResponseEntity<FundDetailsDto> getFundDetails(@PathVariable(value="ticker") String ticker) {
        return ResponseEntity.ok().body(
                fundService.fundDetailsWithTicker(ticker));
    }
}
