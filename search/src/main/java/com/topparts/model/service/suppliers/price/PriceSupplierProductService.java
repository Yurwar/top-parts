package com.topparts.model.service.suppliers.price;

import com.topparts.model.dto.PagedPriceListDTO;
import com.topparts.model.entity.Product;
import com.topparts.model.entity.Supplier;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.suppliers.SupplierService;
import com.topparts.model.task.RecursivePageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceSupplierProductService implements ProductService {
    private static final String PRICE_SUPPLIER_NAME = "Price supplier";
    private RestTemplate restTemplate;
    private String priceSupplierUrl;
    private SupplierService supplierService;

    public PriceSupplierProductService(RestTemplateBuilder builder,
                                       SupplierService supplierService,
                                       @Value("${suppliers.price.url}") String priceSupplierUrl) {
        this.restTemplate = builder.build();
        this.supplierService = supplierService;
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

    public List<Product> getProductsByPage(int page) {
        log.trace("Trying to get all products from page #{}", page);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));

        PagedPriceListDTO priceListPage = executeQuery(headers, params);

        if (priceListPage == null) {
            return Collections.emptyList();
        }

        return mapPriceListPageToProductsList(priceListPage);
    }

    @Override
    @Cacheable(value = "priceSupplierProducts")
    public List<Product> getAllProducts() {

        PagedPriceListDTO priceListPage = executeQuery();

        List<Product> result = mapPriceListPageToProductsList(priceListPage);

        Long totalPages = priceListPage.getTotalPages();

        if (totalPages > 1) {
            result.addAll(getProductsAsParallelTask(totalPages));
        }

        fillSupplierForProducts(result);

        return result;
    }

    private void fillSupplierForProducts(List<Product> result) {
        Supplier priceSupplier = supplierService.getByName(PRICE_SUPPLIER_NAME);
        result.forEach(product -> product.setSupplier(priceSupplier));
    }

    private PagedPriceListDTO executeQuery() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        Map<String, String> params = new HashMap<>();

        return executeQuery(headers, params);
    }

    private PagedPriceListDTO executeQuery(HttpHeaders headers, Map<String, String> params) {
        String priceListResourceUrl = priceSupplierUrl + "/price-list";

        ResponseEntity<PagedPriceListDTO> priceListResponseEntity = restTemplate
                .exchange(priceListResourceUrl,
                        HttpMethod.GET,
                        new HttpEntity(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        params);

        return priceListResponseEntity.getBody();
    }

    private List<Product> mapPriceListPageToProductsList(PagedPriceListDTO priceListPage) {
        String productDetailUrlPattern = priceSupplierUrl + "/details/";

        return priceListPage.getResults().keySet()
                .stream()
                .map(id -> getProduct(priceListPage.getResults(), productDetailUrlPattern, id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    private List<Product> getProductsAsParallelTask(Long totalPages) {
        List<Integer> pages = new ArrayList<>();

        for (int i = 1; i < totalPages; i++) {
            pages.add(i);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        RecursivePageTask recursivePageTask = new RecursivePageTask(pages, this);

        return forkJoinPool.invoke(recursivePageTask);
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
    public void updateProduct(Long id, Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteProduct(Long id) {
        throw new UnsupportedOperationException();
    }
}
