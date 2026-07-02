package com.example.oilbilling.repository;

import com.example.oilbilling.model.SalesReturnRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturnRequests, Long> {

}
