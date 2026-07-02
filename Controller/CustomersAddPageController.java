package com.example.oilbilling.controller;

import com.example.oilbilling.model.Customers;
import com.example.oilbilling.services.CustomersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomersAddPageController {
private final CustomersService customersService;

    public CustomersAddPageController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @GetMapping("/customers/add")
    public String showCustomerAddPage(Model model)
    {
model.addAttribute("customers" , new Customers());
return "AddCustomer";
    }

    @GetMapping("/customers/view")
    public String viewAllCustomers(Model model) {
        model.addAttribute("customers", customersService.getAllCustomers());
        return "/viewCustomers"; // path to your HTML file
    }
    @PostMapping("/customers/save")
public  String saveCustomer(@ModelAttribute("customers") Customers customers)
    {
        customersService.addCustomers(customers);
return "redirect:/customers/view";
    }

}
