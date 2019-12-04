package com.topparts.model.service.impl;

import com.topparts.model.entity.Supplier;
import com.topparts.model.repository.SupplierRepository;
import com.topparts.model.service.suppliers.SupplierService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SupplierServiceImpl implements SupplierService {
    private SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier getByName(String name) {
        return supplierRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("No such supplier with name: " + name));
    }
}
