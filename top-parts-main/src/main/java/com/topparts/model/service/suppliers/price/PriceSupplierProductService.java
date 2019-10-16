package com.topparts.model.service.suppliers.price;

import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
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

    @Override
    public List<Product> getAllProducts() {

        String priceListResourceUrl = priceSupplierUrl + "/price-list";
        ResponseEntity<Map<Long, Double>> priceListResponseEntity = restTemplate
                .exchange(priceListResourceUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Map<Long, Double> priceListMap = priceListResponseEntity.getBody();

        String productDetailUrlPattern = priceSupplierUrl + "/{0}";

        if (priceListMap == null) {
            return Collections.emptyList();
        }

        return priceListMap.keySet()
                .stream()
                .map(id -> getProduct(priceListMap, productDetailUrlPattern, id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Product getProduct(Map<Long, Double> priceListMap, String productDetailUrlPattern, Long id) {
        Product product = restTemplate.getForObject(String.format(productDetailUrlPattern, id), Product.class);

        if (product == null) {
            return null;
        }

        product.setId(id);
        product.setPrice(priceListMap.get(id));
        return product;
    }

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        Pattern pattern = Pattern.compile(".*" + query + ".*", Pattern.CASE_INSENSITIVE);
        return getAllProducts()
                .stream()
                .filter(product -> pattern.matcher(product.getDescription()).matches()
                                || pattern.matcher(product.getName()).matches())
                .collect(Collectors.toList());
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
