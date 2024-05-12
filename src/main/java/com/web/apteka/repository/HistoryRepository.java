package com.web.apteka.repository;

import com.web.apteka.model.CartItemDTO;
import com.web.apteka.model.FavoriteDTO;
import com.web.apteka.model.HistoryItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryItemDTO, Integer> {

    @Query("SELECT c FROM HistoryItemDTO c WHERE c.user_id=:id AND c.status=0 ORDER BY boughtAt DESC")
    List<HistoryItemDTO> getActiveDeliveries(@Param("id") UUID id);

    @Query("SELECT c FROM HistoryItemDTO c WHERE c.user_id=:id AND c.status=1 ORDER BY deliveryDate DESC")
    Page<HistoryItemDTO> getHistory(org.springframework.data.domain.Pageable pageable, @Param("id") UUID id);

    @Query("SELECT COUNT(c) FROM HistoryItemDTO c WHERE c.user_id=:id AND c.status=1")
    Integer getHistorySize(@Param("id") UUID id);
}
