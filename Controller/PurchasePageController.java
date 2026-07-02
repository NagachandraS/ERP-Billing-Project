package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.PurchaseOrderViewResponseDTO;
import com.example.oilbilling.model.PurchaseBillingRequests;
import com.example.oilbilling.services.PurchaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/purchaseOrder")
public class PurchasePageController {

    private final PurchaseService purchaseService;

    public PurchasePageController(PurchaseService purchaseService)
    {
        this.purchaseService=purchaseService;
    }

    @GetMapping("/create")
    public String showPurchaseOrderPage()
    {
        return "PurchaseOrder";
    }

    @GetMapping("/view")
    public String showViewPurchasePage(@RequestParam(defaultValue="0")int page, Model model)
    {
        int pageSize = 10;
        Page<PurchaseBillingRequests> pBillPage=purchaseService.getAllPurchaseInvoices(PageRequest.of(page,pageSize));
        model.addAttribute("PurchaseInvoice", pBillPage.getContent());
        model.addAttribute("currentPage", pBillPage.getNumber());
        model.addAttribute("totalPages", pBillPage.getTotalPages());
        model.addAttribute("pageSize", pBillPage.getSize());
        return "viewPurchases";
    }

    @GetMapping("/viewDetails/{id}")
    public String showPurchaseInvoiceDetails(@PathVariable long id, Model model)
    {
        PurchaseOrderViewResponseDTO purchaseOrderDetails = purchaseService.viewPurchaseOrder(id);
        model.addAttribute("purchaseOrderDetails", purchaseOrderDetails);
        return "/viewPurchaseOrderDetails";
    }
}
