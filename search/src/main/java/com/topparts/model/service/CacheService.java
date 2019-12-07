package com.topparts.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CacheService {
    private final CacheManager cacheManager;
    private final ProductService topPartsGrpcSupplierService;
    private final ProductService searchGrpcSupplierService;
    private final ProductService priceGrpcSupplierService;

    public CacheService(CacheManager cacheManager,
                        ProductService topPartsGrpcSupplierService,
                        ProductService searchGrpcSupplierService,
                        ProductService priceGrpcSupplierService) {
        this.cacheManager = cacheManager;
        this.topPartsGrpcSupplierService = topPartsGrpcSupplierService;
        this.searchGrpcSupplierService = searchGrpcSupplierService;
        this.priceGrpcSupplierService = priceGrpcSupplierService;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void refreshCacheScheduled() {
        log.info("Start refreshing cache");
        for (String name : cacheManager.getCacheNames()) {
            Optional.ofNullable(cacheManager.getCache(name)).ifPresent(Cache::clear);
        }
        log.info("All cache cleared");
        topPartsGrpcSupplierService.getAllProducts();
        searchGrpcSupplierService.getAllProducts();
        priceGrpcSupplierService.getAllProducts();
        log.info("All cache refreshed");
    }

    public void initCache() {
        log.info("Start initializing cache");
        topPartsGrpcSupplierService.getAllProducts();
        searchGrpcSupplierService.getAllProducts();
        priceGrpcSupplierService.getAllProducts();
        log.info("All cache initialized");
    }

    public void clearCacheByName(String name) {
        Optional.ofNullable(cacheManager.getCache(name)).ifPresent(Cache::clear);
    }

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) {
        initCache();
    }
}
