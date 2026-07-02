package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.SalesReturnViewDTO;
import com.example.oilbilling.model.BillingItems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesReturnViewRepository extends JpaRepository<BillingItems, Long> {
    @Query("""
    SELECT new com.example.oilbilling.DTO.SalesReturnViewDTO(
        br.customerName,
        p.productId,
        bi.itemName,
        bi.itemPrice,
        bi.itemQuantity
    )
    FROM BillingItems bi
    JOIN bi.billingRequests br
    JOIN bi.products p
    WHERE br.invoiceId = :invoiceId
""")

    List<SalesReturnViewDTO> getInvoiceDetailsByID(@Param("invoiceId") long invoiceId);
}
