package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.ProductCreatedResponse;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.model.Product;

@Service 
public class ProductServiceImpl {
    private final List<Product> productsInMemory = new ArrayList<>();

    public ProductCreatedResponse create(ProductForCreateDto productDto)
    {
        checkIfProductWithSameNameExist(productDto.getName());
        
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setId(new Random().nextInt(999));

        productsInMemory.add(product);

        ProductCreatedResponse response = new ProductCreatedResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }

    public void update() {
        checkIfProductWithSameNameExist("");
    }

    private void checkIfProductWithSameNameExist(String name) {
        Product productWithSameName = productsInMemory
                                        .stream()
                                        .filter(product->product.getName().equals(name))
                                        .findFirst()
                                        .orElse(null);

        if(productWithSameName != null)
            throw new RuntimeException("Aynı isimde 2 ürün eklenemez");
    }
}