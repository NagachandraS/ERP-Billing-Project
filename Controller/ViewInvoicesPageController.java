package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.InvoiceViewHeaderDTO;
import com.example.oilbilling.DTO.InvoiceViewResponseDTO;
import com.example.oilbilling.model.BillingRequests;
import com.example.oilbilling.services.BillingService;
import com.example.oilbilling.services.PdGenerationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class ViewInvoicesPageController {

    private final BillingService billingService;

    public ViewInvoicesPageController(BillingService billingService )
    {
        this.billingService=billingService;
    }
    @GetMapping("/viewInvoices")
    public String viewInvoicePage(@RequestParam(defaultValue = "0") int page, Model model){
        int pageSize = 10;
        Page<BillingRequests> billingRequestsPage=billingService.getAllInvoices(PageRequest.of(page,pageSize));
      //  List<BillingRequests> billingRequests=billingService.getAllInvoices();
      //  model.addAttribute("Invoices", billingRequests);
        model.addAttribute("Invoices", billingRequestsPage.getContent());
        model.addAttribute("currentPage", billingRequestsPage.getNumber());
        model.addAttribute("totalPages",billingRequestsPage.getTotalPages());
        model.addAttribute("pageSize", billingRequestsPage.getSize());
        return "/viewInvoices";
    }


@GetMapping("/searchInvoice")
    public String searchInvoice(@RequestParam("InvoiceId") int InvoiceId,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int pageSeize,
                                Model model )
{
    Page<BillingRequests> filteredInvoices= billingService.findByInvoiceNumber(InvoiceId, PageRequest.of(page,pageSeize));
    model.addAttribute("Invoices", filteredInvoices);
    model.addAttribute("totalPages", filteredInvoices.getTotalPages());
    model.addAttribute("currentPage",filteredInvoices.getNumber());
    return "/viewInvoices";
}

@GetMapping("/viewInvoice/{id}")
    public String getBillDetailsPage(@PathVariable  long id, Model model)
{
     InvoiceViewResponseDTO invoiceDetails =billingService.viewInvoice(id);
    model.addAttribute("invoiceDetails",invoiceDetails );
    return "/viewInvoiceDetails";
}

@GetMapping("/ViewInvoice/print/{id}")
    public ResponseEntity<byte[]> printInvoice(@PathVariable long id) throws IOException {
   byte[] pdfBytes = billingService.generateInvoicePdf(id);

   return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
           "inline; filename=invoice_" + id + ".pdf").contentType((MediaType.APPLICATION_PDF)).body(pdfBytes);
}
}
