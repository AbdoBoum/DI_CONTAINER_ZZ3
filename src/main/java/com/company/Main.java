package com.company;

import com.company.Injector.Container;
import com.company.Services.ProductServiceInterface;

public class Main {

    static ProductServiceInterface productService;
    static Container injector;

    public static void main(String[] args) {
	    injector = new Container();
        productService = injector.getBeanInstance(ProductServiceInterface.class);
        productService.printProduct();
    }
}
