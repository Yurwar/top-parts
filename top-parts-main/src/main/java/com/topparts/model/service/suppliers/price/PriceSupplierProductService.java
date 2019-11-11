package com.topparts.model.service.suppliers.price;

import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public PriceSupplierProductService(RestTemplateBuilder builder,
                                       @Value("${suppliers.price.url}") String priceSupplierUrl) {
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

    @Override
    @Cacheable(value = "priceSupplierProducts")
    public List<Product> getAllProducts() {
        log.trace("Trying to get all products from price supplier");
        String priceListResourceUrl = priceSupplierUrl + "/price-list";

        log.trace("Get price list of products");
        ResponseEntity<Map<Long, Double>> priceListResponseEntity = restTemplate
                .exchange(priceListResourceUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Map<Long, Double> priceListMap = priceListResponseEntity.getBody();

        String productDetailUrlPattern = priceSupplierUrl + "/details/";

        if (priceListMap == null) {
            return Collections.emptyList();
        }

        log.trace("Get products by id from price list");
        List<Product> products = priceListMap.keySet()
                .stream()
                .map(id -> getProduct(priceListMap, productDetailUrlPattern, id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.trace("Return all products");
        return products;
    }

    private Product getProduct(Map<Long, Double> priceListMap, String productDetailUrlPattern, Long id) {
        Product product = restTemplate.getForObject(productDetailUrlPattern + id, Product.class);

        if (product == null) {
            return null;
        }

        product.setId(id);
        product.setPrice(priceListMap.get(id));
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
}
