package com.example.oilbilling.repository;

import com.example.oilbilling.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomersRepository extends JpaRepository<Customers, Long>
{
    List<Customers> findByCustomerNameContainingIgnoreCase(String name);
    Customers findCustomerEntityByCustomerId(long customerId );
    List<Customers> findByisActiveTrue();
   // Customers updateCustomerEntityByCustomerId(Customers customer);

}
