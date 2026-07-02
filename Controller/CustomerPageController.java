package com.example.oilbilling.controller;

import com.example.oilbilling.model.Customers;
import com.example.oilbilling.services.CustomersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerPageController {


@GetMapping("/customers")
    public String showCustomerPage(Model model){

        return "customers";
    }


}
