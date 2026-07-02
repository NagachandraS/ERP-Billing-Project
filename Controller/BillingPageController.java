package com.example.oilbilling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BillingPageController {

@GetMapping("/billing")
        public String showBillingPage(Model model)
        {
            return "BillingPage";
        }


}
