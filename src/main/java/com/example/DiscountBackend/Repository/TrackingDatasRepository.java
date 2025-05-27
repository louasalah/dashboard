package com.example.DiscountBackend.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.TrackingData;
@Repository

public interface TrackingDatasRepository extends JpaRepository<TrackingData, Long> {

	    List<TrackingData> findByIdproduct(Long idproduct);

	    Optional<TrackingData> findByIdproductAndPagename(Long idproduct, String pagename);

	    @Query("SELECT t FROM TrackingData t WHERE t.idproduct = :idproduct ORDER BY t.entryTimeFormatted DESC")
	    List<TrackingData> findTopByIdproductOrderByEntryTimeDesc(@Param("idproduct") Long idproduct);

	    Optional<TrackingData> findByIdproductAndSessionId(Long idproduct, String sessionId);

	    // 📊 Statistiques générales

	    @Query("SELECT SUM(t.timespent) FROM TrackingData t")
	    Long getTotalTimeSpent();

	    @Query("SELECT AVG(t.timespent) FROM TrackingData t")
	    Double getAverageTimeSpent();

	    // 🥇 Produits les plus cliqués
	    @Query("SELECT t.idproduct, SUM(t.clicks) FROM TrackingData t GROUP BY t.idproduct ORDER BY SUM(t.clicks) DESC")
	    List<Object[]> findTopProductsByClicks();

	    // 🕒 Produits avec le plus de temps passé
	    @Query("SELECT t.idproduct, SUM(t.timespent) FROM TrackingData t GROUP BY t.idproduct ORDER BY SUM(t.timespent) DESC")
	    List<Object[]> findTopProductsByTimeSpent();

	    // 👁️ Produits les plus vus (nombre total de tracking entries)
	    @Query("SELECT t.idproduct, COUNT(t.id) as views FROM TrackingData t GROUP BY t.idproduct ORDER BY views DESC")
	    List<Object[]> findTopViewedProducts();

	    // ✅ Produits avec nombre de vues total
	    @Query("SELECT t.idproduct, COUNT(t.id) as views FROM TrackingData t GROUP BY t.idproduct")
	    List<Object[]> countViewsPerProduct();

	    // ✅ Total de vues pour un produit spécifique
	    @Query("SELECT COUNT(t.id) FROM TrackingData t WHERE t.idproduct = :idproduct")
	    Long countViewsByProduct(@Param("idproduct") Long idproduct);


}
