package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.InvoiceViewItemsDTO;
import com.example.oilbilling.model.BillingItems;
import com.example.oilbilling.model.BillingRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewInvoiceItemsRepository extends JpaRepository<BillingItems, Long> {

    @Query("""
            select new com.example.oilbilling.DTO.InvoiceViewItemsDTO(
            bi.itemName,
            bi.itemQuantity,
            bi.itemPrice)
           
            from BillingItems bi
            join bi.billingRequests br
            where br.invoiceId=:invoiceId
            """)

     List<InvoiceViewItemsDTO> getInvoiceViewItems(@Param("invoiceId") long invoiceId);
}
