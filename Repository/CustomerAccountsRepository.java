package com.example.oilbilling.repository;

import com.example.oilbilling.model.CustomerAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountsRepository extends JpaRepository<CustomerAccounts, Long> {


}
