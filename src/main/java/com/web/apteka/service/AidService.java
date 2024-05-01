package com.web.apteka.service;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.AidDTO;
import com.web.apteka.repository.AccountRepository;
import com.web.apteka.repository.AidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AidService {
    @Autowired
    private AidRepository aidRepository;

    public AidDTO createAid(AidDTO aid) {
        return aidRepository.save(aid);
    }

    public List<AidDTO> getAllAids() {
        return aidRepository.findAll();
    }
    public Page<AidDTO> getActiveAids(Pageable pageable) {
        return aidRepository.getActiveAids(pageable);
    }
    public Page<AidDTO> findAids(Pageable pageable, String search) {
        return aidRepository.searchAids(pageable, search);
    }

    public Optional<AidDTO> getAidById(Integer id) {
        return aidRepository.findById(id);
    }

    public void deleteAllAids() {
        aidRepository.deleteAll();
    }

    public boolean deleteAid(Integer id) {
        try {
            aidRepository.deleteSoftById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    public boolean recoverAid(Integer id) {
        try {
            aidRepository.recoverAidById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }
//    public AidDTO updateAid(Integer id,AidDTO newData)
//    {
//        try {
//            aidRepository.updateAid(id,
//                    newData.getName(),
//                    newData.getManufacturer(),
//                    newData.getImageURL(),
//                    newData.getDescription(),
//                    newData.getPrice(),
//                    newData.getQuantity());
//        } catch (Exception exception)
//        {
//            System.out.println("Request error: " + exception.getMessage());
//        }
//        return newData;
//    }
    public Integer getAidsCount()
    {
        return aidRepository.getAidsCount();
    }
    public Integer searchAidsCount(String search)
    {
        return aidRepository.getSearchAidsCount(search);
    }
}
