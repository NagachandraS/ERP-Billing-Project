package com.example.oilbilling.repository;

import org.springframework.stereotype.Repository;


import com.example.oilbilling.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases, Long> {



}
