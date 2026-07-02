package com.example.oilbilling.services;


import com.example.oilbilling.model.Inventory;
import com.example.oilbilling.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository=inventoryRepository;
    }

    public Inventory saveInventoryMovement (Inventory inventory)
    {
     return  inventoryRepository.save(inventory);
    }
}
