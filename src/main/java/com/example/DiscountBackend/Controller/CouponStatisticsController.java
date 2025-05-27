package com.example.DiscountBackend.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DiscountBackend.Services.IServiceCouponTracking;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })
@RestController
@RequestMapping("/api/coupons")
public class CouponStatisticsController {

    @Autowired
    private IServiceCouponTracking servTrackCoupon;

    // ✅ Récupérer les statistiques des coupons
    @GetMapping("/couponStatistics")
    public ResponseEntity<Map<String, Object>> getCouponStatistics() {
        try {
            Map<String, Object> statistics = servTrackCoupon.getCouponStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Une erreur est survenue lors de la récupération des statistiques"));
        }
    }
}
