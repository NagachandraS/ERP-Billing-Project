package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.InvoiceViewItemsDTO;
import com.example.oilbilling.DTO.PurchaseOrderViewHeaderDTO;
import com.example.oilbilling.model.BillingRequests;
import com.example.oilbilling.model.PurchaseBillingRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PurchaseOrderViewHeaderRepository extends JpaRepository<PurchaseBillingRequests,Long> {
    @Query("""
            select new com.example.oilbilling.DTO.PurchaseOrderViewHeaderDTO(
            pbr.vendorName,
            pbr.invoiceId,
            pbr.gst,
            pbr.grandTotal)
         
            from PurchaseBillingRequests pbr
            where pbr.invoiceId=:invoiceId
            """
    )
    PurchaseOrderViewHeaderDTO getPurchaseOrderViewHeader(@Param("invoiceId") long invoiceId);
}
