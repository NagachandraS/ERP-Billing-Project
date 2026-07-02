package com.example.oilbilling.repository;

import com.example.oilbilling.model.BillingRequests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BillingRepository extends JpaRepository<BillingRequests, Long> {
public Page<BillingRequests> findBillingRequestByInvoiceId(int id, Pageable pageable);
public BillingRequests findBillingRequestByInvoiceId(long id);
 @Query("SELECT i.invoiceId FROM BillingRequests i WHERE STR(i.invoiceId) LIKE %:keyword%")
 List<Long> searchInvoiceIds(@Param("keyword") String keyword);


}
