package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.PurchaseRequestDTO;
import com.example.oilbilling.DTO.PurchaseRequestItemsDTO;
import com.example.oilbilling.model.Inventory;
import com.example.oilbilling.model.PurchaseBillingRequests;
import com.example.oilbilling.services.InventoryService;
import com.example.oilbilling.services.PurchaseService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/purchaseOrder")
public class PurchaseController {

    private final PurchaseService purchaseService;


    public PurchaseController(PurchaseService purchaseService)
    {
        this.purchaseService=purchaseService;

    }

    @PostMapping("/save")
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseRequestDTO purchaseRequestDTO)
    {
        purchaseService.createPurchase(purchaseRequestDTO );
        return ResponseEntity.ok("invoice saved");
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public List<PurchaseRequestItemsDTO> fileUpload(@RequestParam("pdfFile") MultipartFile file) throws IOException {

        return  purchaseService.updateItemsListFromPDF(file);
    }


}
