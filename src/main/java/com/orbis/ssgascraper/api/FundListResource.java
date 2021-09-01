package com.orbis.ssgascraper.api;

import com.orbis.ssgascraper.dto.FundDto;
import com.orbis.ssgascraper.service.FundsService;
import com.orbis.ssgascraper.transformer.FundTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundListResource {

    private final FundsService fundsService;

    @GetMapping("/funds")
    public ResponseEntity<List<FundDto>> getFunds() {
        return ResponseEntity.ok().body(
                fundsService.getFunds().stream()
                        .map(f -> FundTransformer.toDto(f))
                        .collect(Collectors.toList()));
    }
}
