package com.example.oilbilling.controller;

import com.example.oilbilling.DTO.Report_Daily_Sales_DTO;
import com.example.oilbilling.services.ReportsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;



@RestController
@RequestMapping("/Reports")
public class ReportsAPIController {

    private final ReportsService reportsService;

    public ReportsAPIController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

//    @GetMapping("/DailySales/DSReport")
//    public ResponseEntity<Report_Daily_Sales_DTO> generateReport(
//            @RequestParam("reportDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        ZoneOffset dbZoneOffset = ZoneOffset.ofHoursMinutes(5, 30);
//        OffsetDateTime start = date.atStartOfDay().atOffset(dbZoneOffset);
//        OffsetDateTime end = date.atTime(LocalTime.MAX).atOffset(dbZoneOffset);
//
//        // FIX: Changed second parameter from 'start' to 'end'
//        Report_Daily_Sales_DTO report = reportsService.getDailySalesReport(start, end);
//
//        return ResponseEntity.ok(report);
//    }
}
