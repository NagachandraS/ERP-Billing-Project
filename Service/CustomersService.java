package com.example.oilbilling.services;

import com.example.oilbilling.model.CustomerAccounts;
import com.example.oilbilling.model.Customers;
import com.example.oilbilling.repository.CustomersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
@Service
public class CustomersService {
    private final CustomerAccountsService customerAccountsService;

    private final CustomersRepository customersRepository;

    public CustomersService(CustomerAccountsService customerAccountsService, CustomersRepository customersRepository)
    {
        this.customerAccountsService = customerAccountsService;
        this.customersRepository = customersRepository;
    }

    public Customers addCustomers(Customers customers)
    {

        Customers savedCustomer= customersRepository.save(customers);
        CustomerAccounts customerAccounts= new CustomerAccounts(savedCustomer);
        customerAccounts.setCustomers(customerAccounts.getCustomers());
        customerAccounts.setAccountNumber(customerAccounts.getAccountNumber());
        customerAccountsService.createCustomerAccounts(customerAccounts);

        return savedCustomer;
    }
    public List<Customers> getAllCustomers(){
return  customersRepository.findByisActiveTrue();
        //return customersRepository.findAll();
    }

    public List<Customers> findCustomerContains(String name){
        return customersRepository.findByCustomerNameContainingIgnoreCase(name);
    }

    public Customers  findCustomerToEdit(long customerId){
        return customersRepository.findCustomerEntityByCustomerId(customerId);
    }

    public Customers updateCustomer(Customers customer)
    {
        System.out.println(customer);
        return  customersRepository.save(customer);
    }

    public void deleteCustomer(long id)
    {

     Customers c  = customersRepository.findCustomerEntityByCustomerId(id);
     c.setIsActive(false);
     customersRepository.save(c);
    }
}

