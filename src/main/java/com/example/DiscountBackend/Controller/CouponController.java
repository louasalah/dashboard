package com.example.DiscountBackend.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DiscountBackend.Services.IServiceCoupon;
import com.example.DiscountBackend.Services.IServiceCouponTracking;
import com.example.DiscountBackend.dto.EmailRequest;
import com.example.DiscountBackend.entities.CouponTracking;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private IServiceCoupon ServCoupon;

    @Autowired
    private IServiceCouponTracking ServTrackCoupon;
    

    // 🎯 Envoie de coupon pour un email et un produit spécifique
    @PostMapping("/save")
    public ResponseEntity<?> saveTracking(@RequestBody Map<String, Object> requestData) {
        try {
            String email = (String) requestData.get("email");
            Long idProduct = requestData.get("idproduct") != null ? Long.valueOf(requestData.get("idproduct").toString()) : null;

            ServTrackCoupon.saveTracking(email, null, "send", idProduct);
            return ResponseEntity.ok("Coupon envoyé avec succès !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // 🐞 Debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur.");
        }
    }

    // ✅ Marquer un coupon comme utilisé
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        if ("used".equals(status)) {
            ServTrackCoupon.updateTrackingStatus(id, "used");
            return ResponseEntity.ok("Statut mis à jour en 'used'");
        }
        return ResponseEntity.badRequest().body("Seul 'used' est autorisé.");
    }

    // ✅ Enregistrement d'une demande sans envoi immédiat (statut "Pending")
    @PostMapping("/sendcoupon")
    public ResponseEntity<String> sendcoupon(@RequestBody EmailRequest request) {
        try {
            ServTrackCoupon.saveTracking(
                request.getEmail(),
                request.getCouponCode(), 
                request.getProductReference(),  
                "Pending",  
                request.getIdLink()
            );
            return ResponseEntity.ok("Demande enregistrée.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement de la demande.");
        }
    }

    // ✅ Récupération de tous les suivis
    @GetMapping("/trackinglogs")
    public List<CouponTracking> getAllTrackings() {
        return ServTrackCoupon.getAllTrackings();
    }
}
