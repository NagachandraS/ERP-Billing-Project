package com.example.oilbilling.services;

import com.example.oilbilling.model.CustomerAccounts;
import com.example.oilbilling.repository.CustomerAccountsRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountsService {

    private final CustomerAccountsRepository customerAccountsRepository;


    public CustomerAccountsService(CustomerAccountsRepository customerAccountsRepository) {
        this.customerAccountsRepository = customerAccountsRepository;
    }

    public void createCustomerAccounts(CustomerAccounts customerAccounts )
    {
         customerAccountsRepository.save(customerAccounts);
    }
}
