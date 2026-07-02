package com.example.oilbilling.repository;

import com.example.oilbilling.model.PurchaseBillingRequests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseBillingRequests,Long>
{
    Page<PurchaseBillingRequests> findAll(Pageable pageable);

    @Query("SELECT i.invoiceId FROM PurchaseBillingRequests i WHERE STR(i.invoiceId) LIKE %:keyword%")
    List<Long> searchPurchaseOrderId(@Param("keyword") String keyword);
}
