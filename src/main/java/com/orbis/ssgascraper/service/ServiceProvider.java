package com.orbis.ssgascraper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This helper class is used to provide any service from the service layer.
 * This is used by the ScraperDataDispatcher to get the service and execute the
 * queries.
 */
@Component
public class ServiceProvider {

    private FundService fundService;

    @Autowired
    public ServiceProvider(FundService fundService) {
        this.fundService = fundService;
    }

    public FundService getFundService() {
        return fundService;
    }
}
