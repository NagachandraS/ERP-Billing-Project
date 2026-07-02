package com.example.oilbilling.services;


import com.example.oilbilling.DTO.*;
import com.example.oilbilling.model.*;
import com.example.oilbilling.repository.*;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductsRepository productsRepository;
    private final InventoryRepository inventoryRepository;

    private final PurchaseOrderViewHeaderRepository purchaseOrderViewHeaderRepository;
    private final PurchaseOrderViewItemsRepository purchaseOrderViewItemsRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductsRepository productsRepository,InventoryRepository inventoryRepository, PurchaseOrderViewHeaderRepository purchaseOrderViewHeaderRepository, PurchaseOrderViewItemsRepository purchaseOrderViewItemsRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productsRepository = productsRepository;
        this.inventoryRepository=inventoryRepository;
        this.purchaseOrderViewHeaderRepository=purchaseOrderViewHeaderRepository;
        this.purchaseOrderViewItemsRepository=purchaseOrderViewItemsRepository;
    }

    @Transactional
    public PurchaseBillingRequests createPurchase(PurchaseRequestDTO purchaseRequestDTO)
    {
        String vendorName = purchaseRequestDTO.getVendorName();
        BigDecimal subtotal = purchaseRequestDTO.getSubtotal();
        BigDecimal gst = purchaseRequestDTO.getGst();
        BigDecimal grandTotal = purchaseRequestDTO.getGrandTotal();

        List<PurchaseRequestItemsDTO> itemsDTO = purchaseRequestDTO.getItems();

        List<PurchaseBillingItems> pbiList = new ArrayList<>();

        // STEP 1: Convert DTO → Entity (Items)
        for (PurchaseRequestItemsDTO itemDTO : itemsDTO)
        {
            // Validate quantity & price
            if (itemDTO.getItemPurchaseQuantity().compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            if (itemDTO.getItemPurchasePrice().compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new IllegalArgumentException("Price must be greater than 0");
            }

            // Fetch Product
            Products product = productsRepository
                    .findProductByProductId(itemDTO.getProductId());

            if (product == null)
            {
                throw new IllegalArgumentException("Product not found: " + itemDTO.getProductId());
            }

            // Create Billing Item
            PurchaseBillingItems item = new PurchaseBillingItems(
                    product.getProductName(),
                    itemDTO.getItemPurchaseQuantity(),
                    itemDTO.getItemPurchasePrice(),
                    product
            );

            pbiList.add(item);
        }

        // STEP 2: Create Purchase Bill
        PurchaseBillingRequests pbr = new PurchaseBillingRequests(
                vendorName,
                pbiList,
                subtotal,
                gst,
                grandTotal
        );

        // STEP 3: Set Parent Reference
        for (PurchaseBillingItems item : pbiList)
        {
            item.setPurchaseBillingRequests(pbr);
        }

        // STEP 4: Save Purchase (needed for invoiceId)
        pbr = purchaseRepository.save(pbr);

        // STEP 5: Inventory + Stock Update
        List<Inventory> inventoryList = new ArrayList<>();
        List<Products> productsToUpdate = new ArrayList<>();

        for (PurchaseBillingItems item : pbiList)
        {
            Products product = item.getProducts();
            BigDecimal openingStock = product.getProductQuantity();
            if (openingStock == null)
            {
                openingStock = BigDecimal.ZERO;
            }
            BigDecimal closingStock = openingStock.add(item.getItemPurchaseQuantity());
            // Inventory Entry
            Inventory movement = new Inventory(
                    MovementType.IN,
                    product,
                    item.getItemPurchaseQuantity(),
                    ReferenceType.PURCHASE,
                    pbr.getInvoiceId(),
                    openingStock,
                    closingStock
            );
            inventoryList.add(movement);

            // Update Product Stock
            product.setProductQuantity(closingStock);
            productsToUpdate.add(product);
        }

        // STEP 6: Save Inventory + Product Updates
        inventoryRepository.saveAll(inventoryList);
        productsRepository.saveAll(productsToUpdate);

        return pbr;
    }

    public  Page<PurchaseBillingRequests> getAllPurchaseInvoices(Pageable pageable)
    {
        return purchaseRepository.findAll(pageable);
    }

    public PurchaseOrderViewResponseDTO viewPurchaseOrder(long id)
    {
        PurchaseOrderViewHeaderDTO purchaseOrderViewHeaderDTO = purchaseOrderViewHeaderRepository.getPurchaseOrderViewHeader(id);
        List<PurchaseOrderViewItemsDTO> purchaseOrderViewItemsDTOS= purchaseOrderViewItemsRepository.getPurchaseOrderItemsDTO(id);
        return new PurchaseOrderViewResponseDTO(purchaseOrderViewHeaderDTO,purchaseOrderViewItemsDTOS);
    }

    public List<Long>  getAllPurchaseOrderById (String id)
    {
     return    purchaseRepository.searchPurchaseOrderId(id);
    }

    public List<PurchaseRequestItemsDTO> updateItemsListFromPDF(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        List<PurchaseRequestItemsDTO> purchaseRequestItemsDTOList = new ArrayList<>();
        InputStream iStream=file.getInputStream();
        PDDocument pdDocument=PDDocument.load(iStream);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text= pdfTextStripper.getText(pdDocument);
        System.out.println(text);
        String[] LineString=text.split("\n");
        for (String ls: LineString) {
            //  System.out.println(ls);
            ls= ls.trim();
            if (ls.isEmpty())
            {
                continue;
            }
            boolean startsWithInteger = ls.matches("^\\d+.*");
            boolean hasItemCode= ls.matches(".*\\d{11}.*");
            boolean hasDecimal = ls.matches(".*\\d+\\.\\d+.*");

            if (startsWithInteger && hasItemCode && hasDecimal)
            {
                System.out.println("Item Row Found");
                System.out.println(ls);
                String[] splitItemRows=   ls.split(" ");
                String Amount;
                String btl;
                String CB;
                String itemCode;
                StringBuilder itemName = new StringBuilder();
                String ratePerPrice;

                Amount=splitItemRows[splitItemRows.length-1];
                btl= splitItemRows[splitItemRows.length-2];
                CB= splitItemRows[splitItemRows.length-3];
                ratePerPrice= splitItemRows[splitItemRows.length-4];
                itemCode=splitItemRows[splitItemRows.length-5];

                for (int i = 1; i<splitItemRows.length-5;i++)
                {
                    itemName.append(" ").append(splitItemRows[i]);
                }
                System.out.println("Item Name : "+ itemName.toString().trim());
                System.out.println("itemCode : "+ itemCode);
                System.out.println("Rate : "+ ratePerPrice);
                System.out.println("CB : "+ CB);
                System.out.println("Bottle : "+ btl);
                System.out.println("Amount : "+ Amount);
//BigDecimal itemCodeBD= BigDecimal.valueOf(Long.parseLong(itemCode));
BigDecimal cbBigDecimal = new BigDecimal(CB);
BigDecimal ratePerPriceBD= new BigDecimal(ratePerPrice);
long productId= productsRepository.getProductIByProductCode(itemCode);

             PurchaseRequestItemsDTO purchaseRequestItemsDTO = new PurchaseRequestItemsDTO(itemName,itemCode,cbBigDecimal,ratePerPriceBD,productId);
                purchaseRequestItemsDTOList.add(purchaseRequestItemsDTO);
            }

        }
        return purchaseRequestItemsDTOList;
    }
    }