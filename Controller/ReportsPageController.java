package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.*;
import com.example.oilbilling.services.ReportsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Controller
@RequestMapping("/Reports")
public class ReportsPageController {

    private final ReportsService reportsService;

    public ReportsPageController(ReportsService reportsService)
    {
        this.reportsService=reportsService;
    }

@GetMapping("/")
    public String getReportsPage ()
    {
        return "Reports";
    }

@GetMapping("/DailySales")
public String getReportsDailySalesReport()
{
    return "Reports_DailySales";
}


    @GetMapping("/DailySales/DSReport")
    public String generateReport(
            @RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {

        ZoneOffset dbZoneOffset = ZoneOffset.ofHoursMinutes(5, 30);
        Instant start = date.atStartOfDay().atOffset(dbZoneOffset).toInstant();
        Instant end = date.atTime(LocalTime.MAX).atOffset(dbZoneOffset).toInstant();

        // FIX: Changed second parameter from 'start' to 'end'
        Report_Daily_Sales_DTO report = reportsService.getDailySalesReport(start, end);
        System.out.println(report);
        model.addAttribute("report", report);
        model.addAttribute("selectedDate", date);

        return "Reports_DailySales_Data";
    }

    @GetMapping("/DailySalesByProducts")
    public String getDailySalesByProductPage()
    {
        return "Reports_DailySalesByProducts";
    }

    @GetMapping("/DailySalesByProducts/DSPReports")
    public String getDailySalesByProductPageData(@RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model)
    {

        ZoneOffset dbZoneOffset= ZoneOffset.ofHoursMinutes(5,30);
        Instant start = date.atStartOfDay().atOffset(dbZoneOffset).toInstant();
        Instant end = date.atTime(LocalTime.MAX).atOffset(dbZoneOffset).toInstant();
       List<Report_Product_Wise_Daily_Sales_DTO>  report=reportsService.getDailySalesReportByProducts(start,end);
for(Report_Product_Wise_Daily_Sales_DTO re:report )
{
   System.out.println(re.getItemName());
    System.out.println(re.getNumberOfBottles());
}

        model.addAttribute("report", report);
        model.addAttribute("selectedDate", date);
        return "Reports_DailySalesByProducts_Data";
    }

    @GetMapping("/CurrentStocks")
    public String getCurrentStockReport(Model model)
    {
      List<Report_Current_Stock_DTO> report=  reportsService.getCurrentStockReport();
        model.addAttribute("report", report);
      return "Report_Current_Stock_Data";
    }

    @GetMapping("/StockMovement")
    public String getStockMovementReportPage()
    {
        return "Reports_StockMovement";
    }
    @GetMapping("/StockMovement/SMReport")
   public String getStockMovementReport(@RequestParam("fromDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                        @RequestParam("toDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate toDate,
                                        Model model)
    {
        ZoneOffset dbZoneOffset= ZoneOffset.ofHoursMinutes(5,30);
        Instant start=fromDate.atStartOfDay().atOffset(dbZoneOffset).toInstant();
        Instant end=toDate.atTime(LocalTime.MAX).atOffset(dbZoneOffset).toInstant();
        List<Report_Stock_Movement_DTO> report= reportsService.getStockMovementReport(start, end);
        model.addAttribute("fromDate", start);
        model.addAttribute("toDate", end);
        model.addAttribute("report", report);
        for(Report_Stock_Movement_DTO r:report)
        {
            System.out.println(r.getReferenceNumber());
        }
        return "Reports_StockMovement_Data";
    }
    @GetMapping("/LiterStatement")
    public String getLiterStatementPage()
    {
        return "Reports_Liter_Statement";
    }

    @GetMapping("/LiterStatement/LSReport")
    public String getLiterStatementReport(@RequestParam("fromDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                          @RequestParam("toDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate toDate,
                                          Model model)
    {
        ZoneOffset dbDateFormat = ZoneOffset.ofHoursMinutes(5,30);
        Instant start = fromDate.atStartOfDay().atOffset(dbDateFormat).toInstant();
        Instant end =toDate.atTime(LocalTime.MAX).atOffset(dbDateFormat).toInstant();
        BigDecimal openingBalance = reportsService.getOpeningBalance(start);
        BigDecimal closingBalance= reportsService.getClosingBalance(end);
        List<Report_Liter_Statement_DTO> report= reportsService.getLiterStatementReport(start,end);
        model.addAttribute("openingBalance",openingBalance);
        model.addAttribute("closingBalance",closingBalance);
        model.addAttribute("fromDate",fromDate );
        model.addAttribute("toDate",toDate );
        model.addAttribute("report",report);
        return "Reports_Liter_Statement";
    }
}
