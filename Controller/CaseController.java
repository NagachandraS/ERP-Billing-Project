package com.example.oilbilling.controller;

import com.example.oilbilling.model.Cases;
import com.example.oilbilling.services.CasesService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.font.ShapeGraphicAttribute;

@Controller
@RequestMapping("/cases")
public class CaseController {

    private final CasesService casesService;

    public CaseController(CasesService casesService) {
        this.casesService=casesService;
    }

    @GetMapping("/add")
    public String getAddCasePage(Model model)
    {
        model.addAttribute("cases", new Cases());
        return "AddCases";
    }
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveCases(Cases cases)
    {
         casesService.saveCases(cases);
      return   ResponseEntity.ok("Cases Saved Successfully");
    }
}
