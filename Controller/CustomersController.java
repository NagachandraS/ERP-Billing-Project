package com.example.oilbilling.controller;

import com.example.oilbilling.model.CustomerAccounts;
import com.example.oilbilling.model.Customers;
import com.example.oilbilling.services.CustomerAccountsService;
import com.example.oilbilling.services.CustomersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final CustomersService customersService;
    private final CustomerAccountsService customerAccountsService;

    public CustomersController(CustomersService customersService, CustomerAccountsService customerAccountsService) {
        this.customersService = customersService;
        this.customerAccountsService = customerAccountsService;
    }
    @PostMapping
    public Customers addCustomers(@RequestBody Customers customers) {
        return  customersService.addCustomers(customers);
    }
    @GetMapping("/search")
    public List<Customers> searchByName(@RequestParam("name") String name)
    {
        return customersService.findCustomerContains(name);
    }
    @DeleteMapping("/{id}")
      public void deleteCustomer(@PathVariable long id)
    {
        customersService.deleteCustomer(id);
    }
}
