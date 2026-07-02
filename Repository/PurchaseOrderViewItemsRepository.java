package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.PurchaseOrderViewItemsDTO;
import com.example.oilbilling.model.PurchaseBillingItems;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderViewItemsRepository extends JpaRepository<PurchaseBillingItems,Long> {

    @Query("""
            select new com.example.oilbilling.DTO.PurchaseOrderViewItemsDTO(
            pbi.itemName,
            pbi.itemPurchaseQuantity,
            pbi.itemPurchasePrice
            )
            from PurchaseBillingItems pbi
            join pbi.purchaseBillingRequests pbr
            where pbr.invoiceId=:invoiceId
            """)

    List<PurchaseOrderViewItemsDTO> getPurchaseOrderItemsDTO (@Param("invoiceId") long invoiceId);
}
