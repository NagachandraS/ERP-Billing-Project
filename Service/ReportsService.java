package com.example.oilbilling.services;

import com.example.oilbilling.DTO.*;
import com.example.oilbilling.repository.Reports_Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsService {

    private final Reports_Repository reports_repository;

    public   ReportsService(Reports_Repository reports_repository)
    {
        this.reports_repository = reports_repository;
    }

    public Report_Daily_Sales_DTO getDailySalesReport(Instant start, Instant end)
    {
      return   reports_repository.getDailySaleReportData(start,end);
    }

    public List<Report_Product_Wise_Daily_Sales_DTO> getDailySalesReportByProducts(Instant start, Instant end)
    {
        return reports_repository.getDailySaleReportByProductsData(start, end);
    }

    public List<Report_Current_Stock_DTO> getCurrentStockReport()
    {
        return reports_repository.getCurrentStock();
    }
    public List<Report_Stock_Movement_DTO> getStockMovementReport(Instant start, Instant end)
    {
      return   reports_repository.getStockMovementReport(start, end);
    }
    public BigDecimal getOpeningBalance (Instant start)
    {
       return reports_repository.getOpeningBalance(start);
    }

    public BigDecimal getClosingBalance(Instant end)
    {
        return reports_repository.getClosingBalance(end);
    }
    public List<Report_Liter_Statement_DTO> getLiterStatementReport(Instant start, Instant end)
    {
BigDecimal monthlyClosingBalance= BigDecimal.ZERO;
        List<Object[]> rows= reports_repository.getLiterStatementTrx(start,end);
        List<Report_Liter_Statement_DTO> report= new ArrayList<>();
        for(Object[] r:rows) {
            BigDecimal closingBalance = (BigDecimal) r[4];
            monthlyClosingBalance = monthlyClosingBalance.add(closingBalance);
        }

        
        for(Object[] r:rows)
        {
            Report_Liter_Statement_DTO dto = new Report_Liter_Statement_DTO
                    (
                    (Instant) r[0],
                    (long) r[1],
                    (String) r[2],
                    (BigDecimal) r[3],
                    (BigDecimal) r[4], 
                     (BigDecimal) r[5]

                    );
            report.add(dto);
        };
       return report;
    }
}
