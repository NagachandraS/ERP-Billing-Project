package com.example.oilbilling.controller;

import com.example.oilbilling.services.PurchaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/PurchaseReturn")
public class PurchaseReturnPageController {

    private final PurchaseService purchaseService;

    public PurchaseReturnPageController(PurchaseService purchaseService)
    {
        this.purchaseService=purchaseService;
    }

    @GetMapping("/create")
    public String getPurchaseReturnPage()
    {
        return "PurchaseReturn";
    }

    @GetMapping ("/searchInvoiceNumber")
    @ResponseBody
            public List<Long> searchPurchaseOrderById(@RequestParam ("id") String id )
    {
     return    purchaseService.getAllPurchaseOrderById(id);
    }
}
