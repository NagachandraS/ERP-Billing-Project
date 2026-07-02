package com.example.oilbilling.controller;

import com.example.oilbilling.Response.APIResponse;
import com.example.oilbilling.model.Products;
import com.example.oilbilling.services.ProductsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService)
    {
        this.productsService=productsService;
    }

    @PostMapping
    public Products addProducts(@RequestBody Products products)
    {
       return productsService.addProducts(products);
    }

    @GetMapping
    public List<Products> getAllProducts(){
        return productsService.viewProducts();
    }

    @GetMapping("/search")
    public List<Products> findProductsByName(@RequestParam("name") String name) {
        return productsService.findProductByName(name);
    }
    @DeleteMapping("/{id}")
     public void deleteProduct(@PathVariable long id)
      {
      productsService.deleteProduct(id);
            }

     @GetMapping("/barCode/{barCode}")
    public Products getProductByBarCode(@PathVariable ("barCode") String barCode)
     {
        return productsService.getProductByBarCode(barCode);
     }


    }




