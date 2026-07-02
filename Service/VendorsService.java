package com.example.oilbilling.services;

import com.example.oilbilling.model.VendorAccounts;
import com.example.oilbilling.model.Vendors;
import com.example.oilbilling.repository.VendorAccountsRepository;
import com.example.oilbilling.repository.VendorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorsService {

    private final VendorsRepository vendorsRepository;
    private final VendorAccountsService vendorAccountsService;


    public VendorsService(VendorsRepository vendorsRepository, VendorAccountsService vendorAccountsService)
    {
        this.vendorsRepository=vendorsRepository;
        this.vendorAccountsService=vendorAccountsService;
    }

    public Vendors saveVendor(Vendors vendors)
    {
        Vendors savedVendor=   vendorsRepository.save(vendors);

        VendorAccounts vendorAccounts= new VendorAccounts(savedVendor);
        vendorAccountsService.createVendorAccount(vendorAccounts);

return  savedVendor;
    }


    public List<Vendors> getAllVendors ()
    {
        return vendorsRepository.findAll();
    }

    public List<Vendors>  searchVendor(String name)
    {
      return   vendorsRepository.findByVendorNameContainingIgnoreCase(name);
    }
}
