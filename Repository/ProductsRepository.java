package com.example.oilbilling.repository;

import com.example.oilbilling.model.Products;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long>
{
    List<Products> findByproductNameContainingIgnoreCase(String name);
    Products findProductByProductId(long productId);
    @Query("SELECT p.productQuantity FROM Products p WHERE p.productId = :productId")
    BigDecimal getProductQuantityByProductId(@Param("productId") long productId);

    @Query("SELECT p.productId FROM Products p WHERE p.productCode = :productCode")
    long getProductIByProductCode(@Param("productCode") String productCode);

    Products findProductByBarCode(String barCode);

    boolean existsByBarCode(String barCode);

    boolean existsByProductCode(String productCode);



}
