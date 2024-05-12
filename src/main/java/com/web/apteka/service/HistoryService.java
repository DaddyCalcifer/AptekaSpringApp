package com.web.apteka.service;

import com.web.apteka.model.FavoriteDTO;
import com.web.apteka.model.HistoryItemDTO;
import com.web.apteka.repository.FavoriteRepository;
import com.web.apteka.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    public List<HistoryItemDTO> getActive(UUID id)
    {
        return historyRepository.getActiveDeliveries(id);
    }
    public Page<HistoryItemDTO> getHistory(Pageable pageable, UUID id){
        return  historyRepository.getHistory(pageable,id);
    }

    public HistoryItemDTO buyItem(HistoryItemDTO item){
        return historyRepository.save(item);
    }

    public Integer getHistorySize(UUID id)
    {
        return historyRepository.getHistorySize(id);
    }
}
