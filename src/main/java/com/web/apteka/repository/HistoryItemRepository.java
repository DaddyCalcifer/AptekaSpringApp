package com.web.apteka.repository;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.HistoryItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItemDTO, Integer> {
}
