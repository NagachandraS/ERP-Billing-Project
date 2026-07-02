package com.example.oilbilling.services;

import com.example.oilbilling.DTO.SalesReturnItemsDTO;
import com.example.oilbilling.DTO.SalesReturnRequestDTO;
import com.example.oilbilling.model.*;
import com.example.oilbilling.repository.InventoryRepository;
import com.example.oilbilling.repository.ProductsRepository;
import com.example.oilbilling.repository.SalesReturnRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SalesReturnService {

    private final SalesReturnRepository salesReturnRepository;
    private final BillingService billingService;
    private final ProductsRepository productsRepository;

    private final InventoryRepository inventoryRepository;

    public SalesReturnService(SalesReturnRepository salesReturnRepository,BillingService billingService, ProductsRepository productsRepository,InventoryRepository inventoryRepository)
    {
        this.salesReturnRepository=salesReturnRepository;
        this.billingService=billingService;
        this.productsRepository=productsRepository;
        this.inventoryRepository=inventoryRepository;
    }


    @Transactional
public SalesReturnRequests createSalesReturn(SalesReturnRequestDTO salesReturnRequestDTO) {
        String customerName = salesReturnRequestDTO.getCustomerName();
        List<SalesReturnItemsDTO> items = salesReturnRequestDTO.getItems();
        List<SalesReturnItems> ListSRI = new ArrayList<>();
        long invoiceId = salesReturnRequestDTO.getInvoiceId();

        for (SalesReturnItemsDTO dto : items) {

            Products product = productsRepository.findProductByProductId(dto.getProductId());
            SalesReturnItems sri = new SalesReturnItems(dto.getItemName(),
                    dto.getItemPrice(),
                    dto.getReturnedQuantity(),
                    dto.getOrderedQuantity(), product);
            ListSRI.add(sri);
        }

        BillingRequests billingRequests = billingService.findByInvoiceNumber(invoiceId);

        SalesReturnRequests SRR = new
                SalesReturnRequests(
                billingRequests,
                customerName,
                ListSRI,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(110));
        for (SalesReturnItems sri : ListSRI)
        {
            sri.setSalesReturnRequests(SRR);
        }
        salesReturnRepository.save(SRR);

        List<Inventory> inventoryToUpdate = new ArrayList<>();
        Map<Long, BigDecimal> stockMap = new HashMap<>();
        Map<Long, Products> productEntityMap = new HashMap<>();

        for (SalesReturnItems sri : ListSRI) {

            Long productId = sri.getProducts().getProductId();

            // Initialize stock only once per product
            if (!stockMap.containsKey(productId)) {
                stockMap.put(productId, sri.getProducts().getProductQuantity());
                productEntityMap.put(productId, sri.getProducts());
            }

            BigDecimal opening = stockMap.get(productId);
            BigDecimal closing = opening.add(sri.getReturnedQuantity());

            // Create inventory record
            Inventory inventory = new Inventory(
                    MovementType.IN,
                    sri.getProducts(),
                    sri.getReturnedQuantity(),
                    ReferenceType.SALES_RETURN,
                    SRR.getId(),
                    opening,
                    closing
            );

            inventoryToUpdate.add(inventory);
            stockMap.put(productId, closing);
        }

        for (Map.Entry<Long, BigDecimal> entry : stockMap.entrySet()) {
            Products product = productEntityMap.get(entry.getKey());
            product.setProductQuantity(entry.getValue());
        }

        inventoryRepository.saveAll(inventoryToUpdate);
        productsRepository.saveAll(productEntityMap.values());

return SRR;
    }

}
