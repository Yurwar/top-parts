package com.topparts.model.service.suppliers.price;

import com.fasterxml.jackson.core.type.TypeReference;
import com.topparts.model.dto.PriceListRowDTO;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import com.topparts.model.utils.RestPageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceSupplierProductService implements ProductService {

    private RestTemplate restTemplate;
    private String priceSupplierUrl;

    public PriceSupplierProductService(RestTemplateBuilder builder,  @Value("${suppliers.price.url}") String priceSupplierUrl) {
        this.restTemplate = builder.build();
        this.priceSupplierUrl = priceSupplierUrl;
    }

    @Override
    public void createProduct(Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        throw new UnsupportedOperationException();
    }

//    @Override
//    @Cacheable(value = "priceSupplierProducts")
//    public List<Product> getAllProducts() {
//        log.trace("Trying to get all products from price supplier");
//        String priceListResourceUrl = priceSupplierUrl + "/price-list";
//
//        log.trace("Get price list of products");
//        ResponseEntity<Map<Long, Double>> priceListResponseEntity = restTemplate
//                .exchange(priceListResourceUrl,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<>() {
//                        });
//
//        Map<Long, Double> priceListMap = priceListResponseEntity.getBody();
//
//        String productDetailUrlPattern = priceSupplierUrl + "/details/";
//
//        if (priceListMap == null) {
//            return Collections.emptyList();
//        }
//
//        log.trace("Get products by id from price list");
//        List<Product> products = priceListMap.keySet()
//                .stream()
//                .map(id -> getProduct(priceListMap, productDetailUrlPattern, id))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//        log.trace("Return all products");
//        return products;
//    }

    public List<Product> getProductsByPage(int page) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));

        HttpEntity entity = new HttpEntity(headers);

        String priceListResourceUrl = priceSupplierUrl + "/price-list";
        ResponseEntity<RestPageImpl<PriceListRowDTO>> priceListResponseEntity = restTemplate
                .exchange(priceListResourceUrl,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<>() {},
                        params);

        Page<PriceListRowDTO> priceListPage = priceListResponseEntity.getBody();

        String productDetailUrlPattern = priceSupplierUrl + "/details/";

        if (priceListPage == null) {
            return Collections.emptyList();
        }

        List<Product> result = new ArrayList<>();

        return priceListPage
                .stream()
                .map(priceListRowDTO -> getProduct(priceListRowDTO, productDetailUrlPattern))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "priceSupplierProducts")
    public List<Product> getAllProducts() {
        log.trace("Trying to get all products from price supplier");
        String priceListResourceUrl = priceSupplierUrl + "/price-list";

        log.trace("Get price list of products");
        ResponseEntity<RestPageImpl<PriceListRowDTO>> priceListResponseEntity = restTemplate
                .exchange(priceListResourceUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Page<PriceListRowDTO> priceListPage = priceListResponseEntity.getBody();

        String productDetailUrlPattern = priceSupplierUrl + "/details/";

        if (priceListPage == null) {
            return Collections.emptyList();
        }

        List<Product> result = new ArrayList<>();

        log.trace("Get products by id from price list");
        List<Product> products = priceListPage
                .stream()
                .map(priceListRowDTO -> getProduct(priceListRowDTO, productDetailUrlPattern))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.trace("Return all products");

        int totalPages = priceListPage.getTotalPages();
        log.trace("Total rows: {}", products.size());

        if (totalPages > 1) {
            for (int i = 1; i < totalPages; i++) {
                result.addAll(getProductsByPage(i));
                log.trace("Received paged #{}", i);
            }
        }
        return products;
    }

    private Product getProduct(PriceListRowDTO priceListRowDTO, String productDetailUrlPattern) {
        log.trace("Get product by id from price supplier: {}", priceListRowDTO.getId());

        Product product = restTemplate.getForObject(productDetailUrlPattern + priceListRowDTO.getId(), Product.class);

        if (product == null) {
            return null;
        }

        product.setId(priceListRowDTO.getId());
        product.setPrice(priceListRowDTO.getPrice());
        return product;
    }

    @Override
    @Cacheable(value = "priceSupplierProducts", key = "#query.toLowerCase().trim()")
    public List<Product> getAllProductsBySearchQuery(String query) {
        log.trace("Trying to get all products from price supplier by query: {}", query);

        Pattern pattern = Pattern.compile(".*" + query + ".*", Pattern.CASE_INSENSITIVE);
        List<Product> allProducts = getAllProducts();

        log.trace("Filter all products by pattern matcher");
        List<Product> productsByQuery = allProducts
                .stream()
                .filter(product -> pattern.matcher(product.getDescription()).matches()
                        || pattern.matcher(product.getName()).matches())
                .collect(Collectors.toList());
        log.trace("Return all products by query");
        return productsByQuery;
    }

    @Override
    public List<Product> getAllProductsByCategory(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateProduct(Long id, Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteProduct(Long id) {
        throw new UnsupportedOperationException();
    }

    @Scheduled(cron = "0 9 * * *")
    @CacheEvict(value = "priceSupplierProducts", allEntries = true)
    public void resetCache() {
    }
}
