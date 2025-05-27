	package com.example.DiscountBackend.Services;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Service;
	import com.example.DiscountBackend.Repository.CouponTrackingRepository;
	import com.example.DiscountBackend.Repository.LinkDiscDefRepository;
	import com.example.DiscountBackend.Repository.ProduitRepository;
	import com.example.DiscountBackend.entities.CouponTracking;
	import com.example.DiscountBackend.entities.LinkProdDisc;
	import com.example.DiscountBackend.entities.Produit;
	
	import java.time.DayOfWeek;
	import java.time.LocalDateTime;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Optional;
	import java.util.UUID;
	
	@Service
	public class ServiceCouponTracking implements IServiceCouponTracking {
	
	    @Autowired
	    private CouponTrackingRepository couponTrackingRepository;
	    @Autowired
	    private ProduitRepository prd;
	    @Autowired
	    private LinkDiscDefRepository LDP;
	    @Autowired
	    private MailService mailService;
	
	    // ✅ Save tracking & send coupon
	
	    @Override
	    public void saveTracking(String email, String productReference, String status, Long idLink) {
	        Produit produit = prd.findByIdproduct(idLink).orElse(null);
	        if (produit == null) {
	            throw new IllegalStateException("Produit introuvable.");
	        }

	        Integer qte = produit.getQuantiteDiscount();
	        if (qte == null) {
	            throw new IllegalStateException("La quantité de remise pour ce produit est indéfinie.");
	        }

	        boolean alreadyExists = couponTrackingRepository.existsByEmailAndIdLinkAndStatus(email, idLink, "send");

	        if (qte > 0 && alreadyExists) {
	            throw new IllegalArgumentException("Ce mail a déjà reçu un coupon pour ce produit.");
	        }

	        CouponTracking tracking = new CouponTracking();
	        tracking.setEmail(email);
	        tracking.setCouponCode(generateCouponCode());
	        tracking.setProductReference(produit.getReferenceProduct());
	        tracking.setStatus("send");
	        tracking.setIdLink(idLink);
	        tracking.setSentDate(LocalDateTime.now());

	        couponTrackingRepository.save(tracking);

	        if (qte > 0) {
	            produit.setQuantiteDiscount(qte - 1);
	            prd.save(produit);
	        }

	        mailService.sendCouponEmail(email, tracking.getCouponCode());
	    }

	

	    // ✅ Generate a random coupon code
	    private String generateCouponCode() {
	        return "DISCOUNT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	    }
	
	    // ✅ Get all tracking records
	    @Override
	    public List<CouponTracking> getAllTrackings() {
	        return couponTrackingRepository.findAll();
	    }
	
	    // ✅ Get pending coupons
	    public List<CouponTracking> getPendingCoupons() {
	        return couponTrackingRepository.findByStatus("Pending");
	    }
	
	    // ✅ Update tracking status
	    @Scheduled(fixedRate = 3600000) // Exécution toutes les heures
	    public void updateExpiredCoupons() {
	        LocalDateTime now = LocalDateTime.now();
	        List<CouponTracking> sendCoupons = couponTrackingRepository.findByStatus("send");
	
	        for (CouponTracking coupon : sendCoupons) {
	            LocalDateTime sendDate = coupon.getSentDate();
	            DayOfWeek dayOfWeek = sendDate.getDayOfWeek();
	
	            // Calcul du délai avant expiration
	            LocalDateTime expireTime = sendDate.plusHours((dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) ? 48 : 24);
	
	            if (now.isAfter(expireTime)) {
	                coupon.setStatus("expire");
	                couponTrackingRepository.save(coupon);
	            }
	        }
	    }
	
	    @Override
	    public void updateTrackingStatus(Long id, String newStatus) {
	        CouponTracking tracking = couponTrackingRepository.findById(id).orElse(null);
	       System.out.println(newStatus);
	        if (tracking != null && "send".equals(tracking.getStatus()) && "used".equals(newStatus)) {
	            tracking.setStatus("used");
	            couponTrackingRepository.save(tracking);
	        }
	    }
	
		@Override
		public void saveTracking(String email, String couponCode, String productReference, String status, Long idLink) {
			// TODO Auto-generated method stub
			
		}
		
		
		//statistiques 
		@Override
		 public Map<String, Object> getCouponStatistics() {
		        Map<String, Object> statistics = new HashMap<>();
	
		        // Statistiques générales
		        long totalCouponsSent = couponTrackingRepository.count();
		        long totalCouponsUsed = couponTrackingRepository.countByStatus("used");
		        long totalCouponsSentStatus = couponTrackingRepository.countByStatus("send");
	
		        // Taux de conversion des coupons (utilisés/envoyés)
		        double conversionRate = totalCouponsSentStatus == 0 ? 0 : (double) totalCouponsUsed / totalCouponsSentStatus * 100;
	
		        // Produits les plus populaires (les plus send)
		        List<Object[]> popularProducts = couponTrackingRepository.findMostPopularProducts();
	
		        // Produits les plus populaires en termes d'utilisation(used)
		        List<Object[]> mostUsedProducts = couponTrackingRepository.findMostUsedProducts();
	
		        statistics.put("totalCouponsSent", totalCouponsSent);
		        statistics.put("totalCouponsUsed", totalCouponsUsed);
		        statistics.put("totalCouponsSentStatus", totalCouponsSentStatus);
		        statistics.put("conversionRate", conversionRate);
		        statistics.put("popularProducts", popularProducts);
		        statistics.put("mostUsedProducts", mostUsedProducts);
	
		        return statistics;
		    }
		}
	
