package com.topparts.model.service;

import com.topparts.model.entity.Supplier;

public interface SupplierService {
    Supplier getByName(String name);
}