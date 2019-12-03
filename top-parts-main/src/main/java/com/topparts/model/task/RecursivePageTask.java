package com.topparts.model.task;

import com.topparts.model.entity.Product;
import com.topparts.model.service.suppliers.price.PriceSupplierProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class RecursivePageTask extends RecursiveTask<List<Product>> {
    private PriceSupplierProductService priceSupplierProductService;
    private List<Integer> pagesToLoad;

    public RecursivePageTask(List<Integer> pagesToLoad, PriceSupplierProductService priceSupplierProductService) {
        this.pagesToLoad = pagesToLoad;
        this.priceSupplierProductService  = priceSupplierProductService;
    }

    @Override
    protected List<Product> compute() {
        if (pagesToLoad.size() > 1) {

            List<RecursivePageTask> subTasks = createSubTasks();
            List<Product> result = new ArrayList<>();

            subTasks.forEach(ForkJoinTask::fork);
            subTasks.forEach(subTask -> result.addAll(subTask.join()));

            return result;

        } else {
            return process();
        }
    }

    private List<RecursivePageTask> createSubTasks() {
        return pagesToLoad.stream()
                .map(page -> new RecursivePageTask(Collections.singletonList(page), priceSupplierProductService))
                .collect(Collectors.toList());
    }

    private List<Product> process() {
        return priceSupplierProductService.getProductsByPage(pagesToLoad.get(0));
    }
}
