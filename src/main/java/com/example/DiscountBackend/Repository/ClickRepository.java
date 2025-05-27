package com.example.DiscountBackend.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.Click; // Entité Click que vous devez créer

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
    
    // Récupérer le nombre de clics pour un produit donné
    Optional<Click> findByIdproduct(Long idproduct);

    @Query("SELECT SUM(c.clicks) FROM Click c")
    Long getTotalClicks();

}
