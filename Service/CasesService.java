package com.example.oilbilling.services;

import com.example.oilbilling.exceptions.CasesNotFoundException;
import com.example.oilbilling.model.Cases;
import com.example.oilbilling.repository.CasesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CasesService {

    private final CasesRepository casesRepository;

    public CasesService(CasesRepository casesRepository)
    {
        this.casesRepository=casesRepository;
    }

    public Cases saveCases(Cases cases)
    {
        return casesRepository.save(cases);
    }

    public List<Cases> findAllCases()
    {
        return casesRepository.findAll();
    }
    public Cases getCaseById(Long caseId) {
        return casesRepository.findById(caseId)
                .orElseThrow(() -> new CasesNotFoundException("Case not found: " + caseId));
    }

}
