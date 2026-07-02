package com.example.oilbilling.controller;

import com.example.oilbilling.model.BillingRequests;
import com.example.oilbilling.model.Inventory;
import com.example.oilbilling.services.BillingService;
import com.example.oilbilling.services.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.InsufficientResourcesException;

@RestController
@RequestMapping("/billing")


public class BillingController {
    private final BillingService billingService;
    private final InventoryService inventoryService;
    public BillingController(BillingService billingService, InventoryService inventoryService ) {
        this.billingService = billingService;
        this.inventoryService=inventoryService;
    }

    @PostMapping("/save")
public ResponseEntity<?> invoiceSaving(@RequestBody BillingRequests billingRequests, Inventory inventory) throws InsufficientResourcesException {
    System.out.println("Received invoice: " + billingRequests);
    billingService.createBilling(billingRequests);
    return ResponseEntity.ok("invoice saved");

}
}
