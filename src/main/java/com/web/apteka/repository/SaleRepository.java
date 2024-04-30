package com.web.apteka.repository;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.SaleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<SaleDTO, Integer> {
}
