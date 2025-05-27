package com.example.DiscountBackend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.CouponTracking;

@Repository
public interface CouponTrackingRepository extends JpaRepository<CouponTracking, Long> {
    List<CouponTracking> findByStatus(String status);  // Méthode pour filtrer par statut

    
    //statistuqes
    boolean existsByEmailAndIdLinkAndStatus(String email, Long idLink, String status);

    long countByStatus(String status);


    @Query("SELECT c.productReference, COUNT(c.id) AS couponCount " +
           "FROM CouponTracking c " +
           "WHERE c.status = 'send' " +
           "GROUP BY c.productReference " +
           "ORDER BY couponCount DESC")
    List<Object[]> findMostPopularProducts();  // Produits populaires (selon les coupons envoyés)

    @Query("SELECT c.productReference, COUNT(c.id) AS couponCount " +
           "FROM CouponTracking c " +
           "WHERE c.status = 'used' " +
           "GROUP BY c.productReference " +
           "ORDER BY couponCount DESC")
    List<Object[]> findMostUsedProducts();  // Produits les plus utilisés (selon les coupons utilisés)
}
