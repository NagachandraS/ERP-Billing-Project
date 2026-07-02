package com.example.oilbilling.repository;

import com.example.oilbilling.model.VendorAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorAccountsRepository extends JpaRepository<VendorAccounts,Long> {
}
