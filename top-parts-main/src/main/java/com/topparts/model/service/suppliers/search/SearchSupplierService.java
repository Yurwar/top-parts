package com.topparts.model.service.suppliers.search;

import com.topparts.model.dto.SearchSupplierProductDTO;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
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
public class SearchSupplierService implements ProductService {
    private RestTemplate restTemplate;

    public SearchSupplierService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
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
        String resourceUrl = "http://localhost:8086/search";
        ResponseEntity<List<SearchSupplierProductDTO>> listResponseEntity = restTemplate
                .exchange(resourceUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<SearchSupplierProductDTO>>() {});

        List<SearchSupplierProductDTO> productDTOList = listResponseEntity.getBody();

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

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        throw new UnsupportedOperationException();
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
