package com.example.oilbilling.services;

import com.example.oilbilling.DTO.InvoiceViewHeaderDTO;
import com.example.oilbilling.DTO.InvoiceViewItemsDTO;
import com.example.oilbilling.DTO.InvoiceViewResponseDTO;
import com.example.oilbilling.DTO.SalesReturnViewDTO;
import com.example.oilbilling.exceptions.InsufficientStockException;
import com.example.oilbilling.exceptions.ProductNotFoundException;
import com.example.oilbilling.model.*;
import com.example.oilbilling.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final ViewInvoiceHeaderRepository viewInvoiceHeaderRepository;
    private final ViewInvoiceItemsRepository viewInvoiceItemsRepository;
    private  final PdGenerationService pdGenerationService;

    private final ProductsRepository productsRepository;
    private final InventoryRepository inventoryRepository;

    private final SalesReturnViewRepository salesReturnViewRepository;

    public BillingService(BillingRepository billingRepository, ViewInvoiceHeaderRepository viewInvoiceHeaderRepository, ViewInvoiceItemsRepository viewInvoiceItemsRepository, PdGenerationService pdGenerationService, ProductsRepository productsRepository, InventoryRepository inventoryRepository, SalesReturnViewRepository salesReturnViewRepository) {
        this.billingRepository = billingRepository;
        this.viewInvoiceHeaderRepository = viewInvoiceHeaderRepository;
        this.viewInvoiceItemsRepository= viewInvoiceItemsRepository;
        this.pdGenerationService=pdGenerationService;
        this.productsRepository=productsRepository;
        this.inventoryRepository=inventoryRepository;
        this.salesReturnViewRepository = salesReturnViewRepository;
    }

    @Transactional
    public BillingRequests createBilling(BillingRequests billingRequests) throws InsufficientResourcesException {
        // Ensure all items reference their parent invoice
        BigDecimal subtotal = BigDecimal.ZERO;
        Map<Long, Products> productsMap= new HashMap<>();
        Map<Long, BigDecimal> totalQuantity= new HashMap<>();

        if (billingRequests.getItems() != null) {
            for (BillingItems item : billingRequests.getItems()) {
                Products products = productsRepository.findProductByProductId(item.getProductId());
                if (products == null) {
                    throw new ProductNotFoundException("Product not found for id: " + item.getProductId());
                }
                item.setProducts(products);
                productsMap.put(products.getProductId(), products);
                totalQuantity.merge(products.getProductId(),item.getItemQuantity(), BigDecimal::add);

                if (totalQuantity.get(products.getProductId()).compareTo(products.getProductQuantity())>0 ) {
                    throw new InsufficientStockException("No stocks for this product");
                } else {

                    BigDecimal itemTotal = item.getItemPrice()
                            .multiply((item.getItemQuantity()));
                    subtotal = subtotal.add(itemTotal);
                    item.setBillingRequests(billingRequests);
                }
            }
            billingRequests.setSubTotal(subtotal);
        }
         billingRepository.save(billingRequests);

        for (Long product_id: productsMap.keySet())
        {
            Products product=productsMap.get(product_id);
            Inventory inventory= new Inventory();
            inventory.setProduct(product);
            inventory.setMovementType(MovementType.OUT);
            inventory.setQuantity(totalQuantity.get(product_id));
            BigDecimal openingStock = product.getProductQuantity();
            BigDecimal closingStock=openingStock.subtract(totalQuantity.get(product_id));
            inventory.setClosingStock( closingStock);
            inventory.setOpeningStock(openingStock);
            inventory.setReferenceType(ReferenceType.INVOICE);
            inventory.setReferenceNumber(billingRequests.getInvoiceId());
            inventoryRepository.save(inventory);

            // Updating Products
           // Products product = productsMap.get(product_id);
            product.setProductQuantity(closingStock);
                    productsRepository.save(product);
        }

        return billingRequests;
    }

    public List<BillingRequests> getAllInvoices()
    {
        System.out.println(billingRepository.findAll());
        return billingRepository.findAll();
    }

    public Page<BillingRequests> getAllInvoices(Pageable pageable)
    {
        return billingRepository.findAll(pageable);
    }

    public Page<BillingRequests> findByInvoiceNumber(int id, Pageable pageable)
    {
        return billingRepository.findBillingRequestByInvoiceId(id, pageable);
   }

   public BillingRequests getBillDetails(long id)
       {
           System.out.println( billingRepository.findBillingRequestByInvoiceId(id));
           return billingRepository.findBillingRequestByInvoiceId(id);
       }

       public InvoiceViewResponseDTO viewInvoice(long id)
       {
           InvoiceViewHeaderDTO header=   viewInvoiceHeaderRepository.getInvoiceViewHeader(id);
            List<InvoiceViewItemsDTO> items=viewInvoiceItemsRepository.getInvoiceViewItems(id);
           return  new InvoiceViewResponseDTO(header,items);
       }

       public byte[] generateInvoicePdf(long invoiceId) throws IOException {
           InvoiceViewResponseDTO dto= viewInvoice(invoiceId);
           return pdGenerationService.generateInvoicePDF(dto).toByteArray();
       }

       public List<Long>  FindAllInvoicesById(String id)
       {
           return billingRepository.searchInvoiceIds(id);
       }

    public BillingRequests findByInvoiceNumber(long invoiceId) {
        return billingRepository.findBillingRequestByInvoiceId(invoiceId);
    }

        public List <SalesReturnViewDTO> allRequiredDetailsForReturn(long invoiceId)

        {
            return   salesReturnViewRepository.getInvoiceDetailsByID(invoiceId);
        }
}
