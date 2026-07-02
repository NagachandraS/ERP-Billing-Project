package com.example.oilbilling.controller;

import com.example.oilbilling.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

    private final ProductsService productsService;

    public InventoryController(ProductsService productsService)
    {this.productsService=productsService;

    }

    @GetMapping("/inventory")
    public String getInventoryPage(Model model)

    {
        model.addAttribute("products",productsService.viewProducts());
        return "Inventory";
    }


}
