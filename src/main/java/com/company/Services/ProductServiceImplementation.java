package com.company.Services;

import com.company.Anotations.Service;

@Service
public class ProductServiceImplementation implements ProductServiceInterface {
    @Override
    public void printProduct() {
        System.out.println("Product: en:biscuits-and-cakes");
    }
}
