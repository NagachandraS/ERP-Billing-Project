package com.example.oilbilling.controller;

import com.example.oilbilling.model.Products;
import com.example.oilbilling.services.ProductsService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductsPageController {

    private ProductsService productsService;
    public ProductsPageController (ProductsService productsService)
    {
        this.productsService=productsService;
    }
    @GetMapping("/products")
    public String showProductsPage()
    {

        return "products";
    }



}
