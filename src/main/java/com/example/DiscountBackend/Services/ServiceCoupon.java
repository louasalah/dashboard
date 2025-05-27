package com.example.DiscountBackend.Services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import com.example.DiscountBackend.entities.CouponTracking;
import java.util.List;

@Service
public class ServiceCoupon implements IServiceCoupon {  

    @Autowired
    private ServiceCouponTracking serviceCouponTracking;  // Service pour gérer le suivi des coupons

    @Autowired
    private JavaMailSender mailSender;  // Injecter JavaMailSender pour envoyer des emails

   
    @Override
    @Scheduled(fixedRate = 60000) // Exécution toutes les 60 secondes
    public void autoSendCoupons() {
       
        List<CouponTracking> pendingCoupons = serviceCouponTracking.getPendingCoupons(); // Récupère uniquement les coupons avec statut "Pending"

        for (CouponTracking couponTracking : pendingCoupons) {
            try {
              
                sendCouponEmail(couponTracking.getEmail(), couponTracking.getCouponCode());

                // Mise à jour du statut en "Sent" une fois le coupon envoyé
                couponTracking.setStatus("Sent");
                serviceCouponTracking.saveTracking(couponTracking.getEmail(), couponTracking.getCouponCode(), couponTracking.getProductReference(), "Sent", couponTracking.getIdLink());

            } catch (MessagingException e) {
                // En cas d'erreur, mise à jour du statut en "Failed"
                couponTracking.setStatus("Failed");
                serviceCouponTracking.saveTracking(couponTracking.getEmail(), couponTracking.getCouponCode(), couponTracking.getProductReference(), "Failed", couponTracking.getIdLink());
            }
        }
    }

    // Méthode d'envoi d'email
   @Override
    public void sendCouponEmail(String email, String couponCode) throws MessagingException {
        // Préparer l'email avec JavaMailSender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        
        helper.setTo(email);
        helper.setSubject("Votre coupon de réduction : " + couponCode);
        helper.setText("Félicitations ! Vous avez reçu un coupon de réduction avec le code : " + couponCode, true);
        
        // Envoi de l'email
        mailSender.send(mimeMessage);
    }

}
