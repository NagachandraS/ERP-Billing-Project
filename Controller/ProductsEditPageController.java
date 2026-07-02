package com.example.oilbilling.controller;

import com.example.oilbilling.model.Products;
import com.example.oilbilling.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductsEditPageController {

    private ProductsService productsService;

    public ProductsEditPageController(ProductsService productsService)
    {
        this.productsService=productsService;
    }
    @GetMapping("products/edit/{id}")
    public String getProductEditPage(@PathVariable long id, Model model)
            {
                model.addAttribute("product", productsService.getProductDetails(id));
                return "editProducts";
            }
 @PostMapping("products/update")
    public String updateProduct(@ModelAttribute ("product") Products product, Model model)
    {
        System.out.println("PIDDDDDDDDDDDDDDDDDDDDDDDD"+product.getProductId());
        model.addAttribute("product" , productsService.updateProducts(product));
        return "redirect:/products/view";
    }
}