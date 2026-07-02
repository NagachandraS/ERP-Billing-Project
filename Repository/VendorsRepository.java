package com.example.oilbilling.repository;

import com.example.oilbilling.model.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VendorsRepository extends JpaRepository<Vendors,Long> {

List<Vendors> findByVendorNameContainingIgnoreCase(String name);
}
