package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.InvoiceViewHeaderDTO;
import com.example.oilbilling.DTO.InvoiceViewResponseDTO;
import com.example.oilbilling.DTO.SalesReturnRequestDTO;
import com.example.oilbilling.DTO.SalesReturnViewDTO;
import com.example.oilbilling.model.BillingItems;
import com.example.oilbilling.model.BillingRequests;
import com.example.oilbilling.model.SalesReturnRequests;
import com.example.oilbilling.repository.InventoryRepository;
import com.example.oilbilling.repository.ViewInvoiceHeaderRepository;
import com.example.oilbilling.repository.ViewInvoiceItemsRepository;
import com.example.oilbilling.services.BillingService;
import com.example.oilbilling.services.InventoryService;
import com.example.oilbilling.services.SalesReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/SalesReturn")
public class SalesReturnPageController {

    private final BillingService billingService;
    private final SalesReturnService salesReturnService;

    public SalesReturnPageController(BillingService billingService, SalesReturnService salesReturnService)
    {
        this.billingService=billingService;
        this.salesReturnService=salesReturnService;
    }

    @GetMapping("/Create")
    public String getSalesReturnPage()
    {
        return "SalesReturn";
    }

    @GetMapping ("/searchInvoiceNumber")
    @ResponseBody
    public List<Long> searchInvoiceNumber(@RequestParam ("id") String id )
    {
        return  billingService.FindAllInvoicesById(id);
    }


@GetMapping("/Invoice")
    @ResponseBody
    public List<SalesReturnViewDTO> getAllItemsByInvoiceId(@RequestParam ("id") long id)
    {
        return billingService.allRequiredDetailsForReturn(id);
    }

 @PostMapping("/save")
 public ResponseEntity<?> createSalesReturn (@RequestBody SalesReturnRequestDTO salesReturnRequestDTO)
 {
     salesReturnService.createSalesReturn(salesReturnRequestDTO);
     return ResponseEntity.ok("Return saved successfully");
 }
}
