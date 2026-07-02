package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.InvoiceViewHeaderDTO;
import com.example.oilbilling.model.BillingRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewInvoiceHeaderRepository extends JpaRepository<BillingRequests, Long> {
@Query("""
        SELECT new com.example.oilbilling.DTO.InvoiceViewHeaderDTO(
       br.customerName,
       br.invoiceId,
       br.gst,
       br.grandTotal

        )
        from BillingRequests br
         WHERE br.invoiceId = :invoiceId
        """
)
InvoiceViewHeaderDTO getInvoiceViewHeader(@Param("invoiceId") Long invoiceId);
}
