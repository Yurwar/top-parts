package com.topparts.model.service.suppliers;

import com.topparts.model.entity.Supplier;

public interface SupplierService {
    Supplier getByName(String name);
}
