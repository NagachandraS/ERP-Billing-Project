package com.example.oilbilling.repository;

import com.example.oilbilling.DTO.*;
import com.example.oilbilling.model.BillingRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public interface Reports_Repository extends JpaRepository<BillingRequests,Long> {

    @Query("""
            SELECT new com.example.oilbilling.DTO.Report_Daily_Sales_DTO(
                SUM(i.quantity),
                SUM(i.quantity * p.MlUnits),
                SUM(br.grandTotal)
            )
            FROM BillingRequests br
            JOIN Inventory i ON br.invoiceId = i.referenceNumber
            JOIN i.product p
            WHERE br.createdDate BETWEEN :start AND :end
            """)
    Report_Daily_Sales_DTO getDailySaleReportData(
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Query("""
            SELECT new com.example.oilbilling.DTO.Report_Product_Wise_Daily_Sales_DTO(
                p.productName,
                SUM(i.quantity),
                SUM(i.quantity * p.MlUnits),
                SUM(i.quantity* p.productPrice)
            )
            FROM BillingRequests br
            JOIN Inventory i ON br.invoiceId = i.referenceNumber
            JOIN i.product p
            WHERE br.createdDate BETWEEN :start AND :end
            GROUP BY p.productName
            ORDER BY p.productName
            """)
    List<Report_Product_Wise_Daily_Sales_DTO> getDailySaleReportByProductsData(
            @Param("start") Instant start,
            @Param("end") Instant end);


    @Query("""
            SELECT new com.example.oilbilling.DTO.Report_Current_Stock_DTO(
            p.productName,
            p.MlUnits,
            p.productQuantity,
            p.MlUnits*productQuantity)
            from Products p
            """)
    List<Report_Current_Stock_DTO> getCurrentStock();

    @Query("""
            select new com.example.oilbilling.DTO.Report_Stock_Movement_DTO(
            i.createdDate,
            i.product.productName,
            i.openingStock,
            i.quantity,
            i.movementType,
            i.closingStock,
            i.referenceNumber)
            from Inventory i
                where i.createdDate between :startDate and :endDate
                order by i.createdDate
            """)
    List<Report_Stock_Movement_DTO> getStockMovementReport(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);


    @Query(value = """
            WITH required_result AS (
            SELECT
            i.product_id,
            i.closing_stock,
            ROW_NUMBER() OVER (
                PARTITION BY i.product_id
                ORDER BY i.created_date DESC
            ) AS row_num
            FROM inventory i
            WHERE i.created_date < :fromDate
            )
            SELECT
            COALESCE(SUM(rr.closing_stock * p.ml_units ), 0)
            FROM required_result rr
            JOIN products p
            ON p.product_id = rr.product_id
            WHERE rr.row_num = 1;
                """,
            nativeQuery = true
    )
    BigDecimal getOpeningBalance(@Param("fromDate") Instant fromDate);

    @Query(value = """
            select 
            i.created_date,
            i.reference_number,
            p.product_name,
            i.quantity,
            p.ml_units* i.quantity,
            (i.closing_stock * p.ml_units)
            from inventory i
            join products p on i.product_id= p.product_id
            where i.created_date between :fromDate and :toDate
            order by i.created_date
            """, nativeQuery = true)
    List<Object[]> getLiterStatementTrx(@Param("fromDate") Instant fromDate,
                                                          @Param("toDate") Instant toDate);
    @Query(value = """
            WITH required_result AS (
            SELECT
            i.product_id,
            i.closing_stock,
            ROW_NUMBER() OVER (
                PARTITION BY i.product_id
                ORDER BY i.created_date DESC
            ) AS row_num
            FROM inventory i
            WHERE i.created_date < :toDate
            )
            SELECT
            COALESCE(SUM(rr.closing_stock * p.ml_units ), 0)
            FROM required_result rr
            JOIN products p
            ON p.product_id = rr.product_id
            WHERE rr.row_num = 1;
                """,
            nativeQuery = true
    )
    BigDecimal getClosingBalance(@Param("toDate") Instant fromDate);
}



