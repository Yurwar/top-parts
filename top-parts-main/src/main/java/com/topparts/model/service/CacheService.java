package com.topparts.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
public class CacheService {
    private final CacheManager cacheManager;
    private final ProductService priceSupplierProductService;
    private final ProductService searchSupplierProductService;
    private final ProductService productServiceImpl;

    public CacheService(CacheManager cacheManager,
                        ProductService priceSupplierProductService,
                        ProductService searchSupplierProductService,
                        ProductService productServiceImpl) {
        this.cacheManager = cacheManager;
        this.priceSupplierProductService = priceSupplierProductService;
        this.searchSupplierProductService = searchSupplierProductService;
        this.productServiceImpl = productServiceImpl;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void refreshCacheScheduled() {
        log.info("Start refreshing cache");
        for (String name : cacheManager.getCacheNames()) {
            Optional.ofNullable(cacheManager.getCache(name)).ifPresent(Cache::clear);
        }
        log.info("All cache cleared");
        priceSupplierProductService.getAllProducts();
        searchSupplierProductService.getAllProducts();
        productServiceImpl.getAllProducts();
        log.info("All cache refreshed");
    }

    public void clearCacheByName(String name) {
        Optional.ofNullable(cacheManager.getCache(name)).ifPresent(Cache::clear);
    }
}
