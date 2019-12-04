package com.topparts.model.service.suppliers.search;

import com.topparts.model.dto.SearchSupplierProductDTO;
import com.topparts.model.entity.Product;
import com.topparts.model.entity.Supplier;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.suppliers.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchSupplierProductService implements ProductService {
    private static final String SEARCH_SUPPLIER_NAME = "Search supplier";
    private RestTemplate restTemplate;
    private String searchSupplierUrl;
    private SupplierService supplierService;

    public SearchSupplierProductService(RestTemplateBuilder builder,
                                        SupplierService supplierService,
                                        @Value("${suppliers.search.url}") String searchSupplierUrl) {
        this.restTemplate = builder.build();
        this.supplierService = supplierService;
        this.searchSupplierUrl = searchSupplierUrl;
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
    @Cacheable(value = "searchSupplierProducts")
    public List<Product> getAllProducts() {
        String resourceUrl = searchSupplierUrl + "/search";
        log.trace("Trying to get all products from search supplier: {}", resourceUrl);

        List<SearchSupplierProductDTO> productDTOList = getProductDTOListFromRestTemplate(resourceUrl);
        List<Product> products = extractProductsFromProductDTOS(productDTOList);
        fillSupplierForProducts(products);

        log.trace("Return products");
        return products;
    }

    @Override
    @Cacheable(value = "searchSupplierProducts", key = "#query.toLowerCase().trim()")
    public List<Product> getAllProductsBySearchQuery(String query) {
        String resourceUrl = searchSupplierUrl + "/search?query=" + query;
        log.trace("Trying to get all products from search supplier by query : {}", resourceUrl);

        List<SearchSupplierProductDTO> productDTOList = getProductDTOListFromRestTemplate(resourceUrl);
        List<Product> products = extractProductsFromProductDTOS(productDTOList);
        fillSupplierForProducts(products);

        log.trace("Return products");
        return products;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteProduct(Long id) {
        throw new UnsupportedOperationException();
    }

    private List<SearchSupplierProductDTO> getProductDTOListFromRestTemplate(String resourceUrl) {
        ResponseEntity<List<SearchSupplierProductDTO>> listResponseEntity = restTemplate
                .exchange(resourceUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<SearchSupplierProductDTO>>() {
                        });

        return listResponseEntity.getBody();
    }

    private List<Product> extractProductsFromProductDTOS(List<SearchSupplierProductDTO> productDTOList) {
        log.trace("Extracting product entities from search supplier DTOs");
        if (productDTOList != null) {
            return productDTOList
                    .stream()
                    .map(searchSupplierProductDTO ->
                            Product.builder()
                                    .id(searchSupplierProductDTO.getId())
                                    .name(searchSupplierProductDTO.getName())
                                    .description(searchSupplierProductDTO.getDescription())
                                    .price(searchSupplierProductDTO.getPrice())
                                    .build())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private void fillSupplierForProducts(List<Product> result) {
        Supplier priceSupplier = supplierService.getByName(SEARCH_SUPPLIER_NAME);
        result.forEach(product -> product.setSupplier(priceSupplier));
    }
}
