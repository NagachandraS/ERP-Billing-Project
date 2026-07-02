package com.example.oilbilling.controller;

import com.example.oilbilling.model.Products;
import com.example.oilbilling.services.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsViewPageController {

    private final ProductsService  productsService;

    public ProductsViewPageController (ProductsService productsService)
    {
        this.productsService=productsService;
    }

    @GetMapping("/products/view")
    public String viewAllProducts(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model) {

        {
            Page<Products> productsPage = productsService.getProductsPage(page, size);
            model.addAttribute("products", productsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productsPage.getTotalPages());
            model.addAttribute("pageSize", productsPage.getSize());

           // List<Products> allProducts = productsService.viewProducts();
          //  model.addAttribute("products", allProducts);
            return "viewProducts";
        }

    }
}
