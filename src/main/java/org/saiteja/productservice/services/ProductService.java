package org.saiteja.productservice.services;


import org.saiteja.productservice.exceptions.ProductNotFoundException;
import org.saiteja.productservice.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getSingleProduct(long id) throws ProductNotFoundException;

    Product createProduct(String title,
                          String description,
                          double price,
                          String imageUrl,
                          String category);
}