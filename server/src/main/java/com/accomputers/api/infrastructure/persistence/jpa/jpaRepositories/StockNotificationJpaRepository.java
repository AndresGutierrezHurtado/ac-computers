package com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories;

import com.accomputers.api.infrastructure.persistence.jpa.entities.StockNotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockNotificationJpaRepository extends JpaRepository<StockNotificationEntity, Integer> {
    List<StockNotificationEntity> findByProductId(Integer productId);
    List<StockNotificationEntity> findByEmail(String email);
    List<StockNotificationEntity> findByIsNotified(Boolean isNotified);
    
    @Query("SELECT sn FROM StockNotificationEntity sn WHERE sn.product.id = :productId AND sn.isNotified = false")
    List<StockNotificationEntity> findByProductIdAndNotNotified(@Param("productId") Integer productId);
    
    @Query("SELECT sn FROM StockNotificationEntity sn WHERE sn.email = :email AND sn.isNotified = false")
    List<StockNotificationEntity> findByEmailAndNotNotified(@Param("email") String email);
    
    Optional<StockNotificationEntity> findByProductIdAndEmail(Integer productId, String email);
    
    Page<StockNotificationEntity> findAll(Pageable pageable);
    
    @Query("SELECT sn FROM StockNotificationEntity sn WHERE sn.product.id = :productId")
    Page<StockNotificationEntity> findByProductId(@Param("productId") Integer productId, Pageable pageable);
}

