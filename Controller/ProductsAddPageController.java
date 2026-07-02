package com.example.oilbilling.controller;

import com.example.oilbilling.Response.APIResponse;
import com.example.oilbilling.exceptions.ProductNotFoundException;
import com.example.oilbilling.model.Cases;
import com.example.oilbilling.model.Products;
import com.example.oilbilling.services.CasesService;
import com.example.oilbilling.services.ProductsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProductsAddPageController {

    private final ProductsService productsService;
    private final CasesService casesService;

    public ProductsAddPageController(ProductsService productsService, CasesService casesService)
    {
        this.productsService=productsService;
        this.casesService=casesService;
    }
    @GetMapping("/products/add")
    public String showAddProductsPage(Model model)
    {
        model.addAttribute("products", new Products());
        model.addAttribute("listOfCases", casesService.findAllCases() );
            return "AddProducts";
    }

    @PostMapping("/products/save")
public String saveProducts(@ModelAttribute ("products") Products products,
                           @RequestParam Long caseId)
    {
        Cases cases = casesService.getCaseById(caseId);
        products.setCases(cases);
        productsService.addProducts(products);
        //throw  new ProductNotFoundException("testing exception");
        return "redirect:/products/view";

    }
    @GetMapping("/check/barcode/{barcode}")
    @ResponseBody
    public APIResponse<Boolean> isBarCodePresent (@PathVariable("barcode") String barcode) {
        boolean isPresent = productsService.isBarCodePresent(barcode);
        if (isPresent) {
            return new APIResponse<>(true, "Barcode Already Exist", true);
        }
        return new APIResponse<>(true, "Barcode available", false);
    }

    @GetMapping("check/productCode/{productCode}")
    @ResponseBody
    public APIResponse<Boolean> isProductCodePresent (@PathVariable ("productCode") String productCode)
    {
        boolean present = productsService.isProductCodePresent(productCode);
        if (present)
        {
            return new APIResponse<>(true,"Product Code Already Exist", true);
        }
        else{
            return new APIResponse<>(true,"Product Code available", false);
        }
    }
}
