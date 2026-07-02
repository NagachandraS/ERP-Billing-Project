package com.example.oilbilling.controller;

import com.example.oilbilling.model.Customers;
import com.example.oilbilling.services.CustomersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomersEditPageController {

    private final CustomersService customersService;

    public CustomersEditPageController(CustomersService customersService)
    {
        this.customersService=customersService;
    }

    @GetMapping("/customers/edit/{id}")
    public String navigateToEditPage(@PathVariable("id") long id, Model model)
    {
      //  System.out.println("SEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" + id);
        model.addAttribute("customer",customersService.findCustomerToEdit(id));
        return "/editCustomer";
    }

    @PostMapping("/customers/update/")
    public String updateCustomer(@ModelAttribute("customer") Customers customer , Model model)
    {
        System.out.println("Customer ID is"+customer.getCustomerId());
        model.addAttribute("customer", customersService.updateCustomer(customer));

        return "redirect:/customers/view";
    }


}

