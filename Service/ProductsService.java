package com.example.oilbilling.services;

import com.example.oilbilling.exceptions.ProductNotFoundException;
import com.example.oilbilling.model.Cases;
import com.example.oilbilling.model.Products;
import com.example.oilbilling.repository.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Products addProducts(Products products) {

        if(!isBarCodePresent(products.getBarCode()) && !isProductCodePresent((products.getProductCode())))
        {
            return productsRepository.save(products);
        }
        throw  new ProductNotFoundException("Barcode or product code already exist");
    }

    public List<Products> viewProducts() {
        return productsRepository.findAll();
    }
    public List<Products> findProductByName(String name)
    {
        return productsRepository.findByproductNameContainingIgnoreCase(name);
    }

    public Products getProductDetails(long id)
    {
Products products= productsRepository.findProductByProductId(id);

if (products==null)
{
    throw new ProductNotFoundException("Product with id: "+id+" Not Found");
}
return products;
    }
    public Products updateProducts(Products product)

    {
      return  productsRepository.save(product);
    }

    public void deleteProduct(long id)
    {
        productsRepository.deleteById(id);
    }

    public Page<Products> getProductsPage(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return productsRepository.findAll(pageable);
    }

    public long getProductIdByProductCode(String productCode)
    {
        return productsRepository.getProductIByProductCode(productCode);
    }

    public boolean isBarCodePresent(String barCode)
    {
        return productsRepository.existsByBarCode(barCode);
    }

    public boolean isProductCodePresent(String productCode)
    {
        return productsRepository.existsByProductCode(productCode);
    }
public Products getProductByBarCode(String barCode)
{
   return productsRepository.findProductByBarCode(barCode);
}
}
