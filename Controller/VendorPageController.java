package com.example.oilbilling.controller;


import com.example.oilbilling.model.Vendors;
import com.example.oilbilling.services.VendorsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vendor")
public class VendorPageController {

    private final VendorsService vendorsService;
    public VendorPageController(VendorsService vendorsService)
    {
        this.vendorsService=vendorsService;
    }

    @GetMapping("/add")
    public String getVendorAddPage(Model model )
    {
        model.addAttribute("vendors", new Vendors());
        return "AddVendor";
    }

    @PostMapping("/save")
    public String saveVendor(@ModelAttribute Vendors vendors)
    {
        vendorsService.saveVendor(vendors);
        return "redirect:/vendor/view";
    }

    @GetMapping("/view")
    public String viewAllVendors(Model model)
    {
        model.addAttribute("vendors", vendorsService.getAllVendors());
        return "viewVendors";
    }

    @GetMapping ("/search")
    @ResponseBody
    public List<Vendors> searchVendors (@RequestParam ("name") String name)
    {
       return vendorsService.searchVendor(name);
    }
}
