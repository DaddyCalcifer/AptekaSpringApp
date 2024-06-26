package com.web.apteka.repository;

import com.web.apteka.model.AccountDTO;
import com.web.apteka.model.AidDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AidRepository extends JpaRepository<AidDTO, Integer> {
    @Query("SELECT u FROM AidDTO u WHERE isDeleted = FALSE")
    Page<AidDTO> getActiveAids(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT u FROM AidDTO u WHERE isDeleted = FALSE AND discountPercent > 0 ORDER BY discountPercent DESC")
    List<AidDTO> getSales();

    @Query("SELECT a FROM AidDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY (a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) ASC")
    Page<AidDTO> searchAids(org.springframework.data.domain.Pageable pageable, @Param("search") String search);

    @Query("SELECT a FROM AidDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))" +
            "AND ((a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) BETWEEN :minPrice AND :maxPrice) ORDER BY (a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) ASC")
    Page<AidDTO> searchAidsWithPriceRangeOrderByPriceAsc(org.springframework.data.domain.Pageable pageable,
                                                         @Param("search") String search,
                                                         @Param("minPrice") double minPrice,
                                                         @Param("maxPrice") double maxPrice);


    @Transactional
    @Modifying
    @Query("UPDATE AidDTO a SET a.isDeleted = true WHERE a.id = :id AND a.isDeleted = false")
    void deleteSoftById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE AidDTO a SET a.isDeleted = false WHERE a.id = :id AND a.isDeleted = true")
    void recoverAidById(@Param("id") Integer id);

    //@Transactional
    //@Modifying
    //@Query("UPDATE AidDTO SET " +
    //        "name = :name, " +
    //        "manufacturer = :manufacturer " +
    //        "imageURL = :imageURL " +
    //        "description = :description " +
    //        "price = :price " +
    //        "quantity = :quantity " +
    //        "WHERE id = :id")
    //void updateAid(@Param("id") Integer id,
    //               @Param("name") String name,
    //               @Param("manufacturer") String manufacturer,
    //               @Param("imageURL") String imageURL,
    //               @Param("description") String description,
    //               @Param("price") double price,
    //               @Param("quantity") Integer quantity);
    @Query("SELECT COUNT(*) FROM AidDTO")
    Integer getAidsCount();

    @Query("SELECT COUNT(a) FROM AidDTO a WHERE a.isDeleted = false AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%')))")
    Integer getSearchAidsCount(@Param("search") String search);

    @Query("SELECT COUNT(a) FROM AidDTO a WHERE a.isDeleted = false " +
            "AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND ((a.price * (1 - CAST(a.discountPercent AS DOUBLE) / 100)) BETWEEN :minPrice AND :maxPrice)")
    Integer countSearchAidsWithPriceRangeOrderByPriceAsc(@Param("search") String search,
                                                         @Param("minPrice") double minPrice,
                                                         @Param("maxPrice") double maxPrice);
}
