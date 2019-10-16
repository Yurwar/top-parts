package com.topparts.model.service.suppliers.search;

import com.topparts.model.dto.SearchSupplierProductDTO;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class SearchSupplierProductService implements ProductService {
    private RestTemplate restTemplate;
    private String searchSupplierUrl;

    public SearchSupplierProductService(RestTemplateBuilder builder,
                                        @Value("${suppliers.search.url}") String searchSupplierUrl) {
        this.restTemplate = builder.build();
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
    public List<Product> getAllProducts() {
        String resourceUrl = searchSupplierUrl + "/search";
        List<SearchSupplierProductDTO> productDTOList = getProductDTOListFromRestTemplate(resourceUrl);
        return extractProductsFromProductDTOS(productDTOList);
    }

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        String resourceUrl = searchSupplierUrl + "/search?query=" + query;
        List<SearchSupplierProductDTO> productDTOList = getProductDTOListFromRestTemplate(resourceUrl);
        return extractProductsFromProductDTOS(productDTOList);
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
        if (productDTOList != null) {
            return productDTOList
                    .stream()
                    .map(searchSupplierProductDTO ->
                            Product.builder()
                                    .name(searchSupplierProductDTO.getName())
                                    .description(searchSupplierProductDTO.getDescription())
                                    .price(searchSupplierProductDTO.getPrice())
                                    .build())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
