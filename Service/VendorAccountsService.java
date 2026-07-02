package com.example.oilbilling.services;

import com.example.oilbilling.model.VendorAccounts;
import com.example.oilbilling.model.Vendors;
import com.example.oilbilling.repository.VendorAccountsRepository;
import org.hibernate.bytecode.enhance.VersionMismatchException;
import org.springframework.stereotype.Service;

@Service
public class VendorAccountsService {

    private final VendorAccountsRepository vendorAccountsRepository;

    public VendorAccountsService(VendorAccountsRepository vendorAccountsRepository)
    {
        this.vendorAccountsRepository=vendorAccountsRepository;
    }

public VendorAccounts createVendorAccount(VendorAccounts vendorAccounts)
{
    return vendorAccountsRepository.save(vendorAccounts);
}

}
