package com.example.DiscountBackend.Controller;


import com.example.DiscountBackend.Services.IServiceTracking;
import com.example.DiscountBackend.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;
    
    @Autowired
    private IServiceTracking servTrack;

    // 🎯 Statistiques globales : produits, coupons, vues, etc.
    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> getGlobalStatistics() {
        Map<String, Object> stats = statisticService.generateGlobalStats();
        return ResponseEntity.ok(stats);
    }

    // 🎯 Produits ayant dépassé le seuil de vues
    @GetMapping("/triggered-products")
    public ResponseEntity<List<Long>> getTriggeredProducts() {
        List<Long> products = statisticService.getProductsReachedMaxViews();
        return ResponseEntity.ok(products);
 
    }
    //coupon used * nbr visite/total coupon
    @GetMapping("/engagement-score")
    public ResponseEntity<Map<String, Double>> getEngagementScore() {
        double engagementScore = statisticService.computeEngagementScore();

        return ResponseEntity.ok(
                Collections.singletonMap("engagementScore", engagementScore)
        );
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Map<String, Object>> getStatsByProduct(@PathVariable Long id) {
        Map<String, Object> productStats = statisticService.getProductStats(id);
        return ResponseEntity.ok(productStats);
    }

    @GetMapping("/categories/frequency")
    public ResponseEntity<List<Map<String, Object>>> getCategoryFrequency() {
        return ResponseEntity.ok(statisticService.getMostFrequentCategories());
    }

    @GetMapping("/positions/frequent")
    public ResponseEntity<Map<String, Double>> getMostFrequentPosition() {
        return ResponseEntity.ok(statisticService.getMostFrequentPosition());
    }
    @GetMapping("/peak-hour")
    public ResponseEntity<Map<String, Object>> getPeakHour() {
        return ResponseEntity.ok(statisticService.getPeakHourOfTracking());
    }
    
    @GetMapping("/run-discount-batch")
    public ResponseEntity<String> runBatchNow() {
        statisticService.renewTopProductDiscounts(); // 👈 appelle la méthode manuellement
        return ResponseEntity.ok("Batch de renouvellement exécuté manuellement !");
    }
    
    @GetMapping("/most-viewed-products")	
    public List<Map<String, Object>> getMostViewedProductsRaw() {
        return statisticService.getTop5ProductsByStats();
    }
    
    
    


}
