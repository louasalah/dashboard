package com.example.DiscountBackend.Services;

import com.example.DiscountBackend.Repository.*;
import com.example.DiscountBackend.entities.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StatisticService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CouponTrackingRepository couponTrackingRepository;

    @Autowired
    private TrackingDataRepository trackingDataRepository;
    

    @Autowired
    private LinkDiscDefRepository linkProdDiscRepository;


    @Autowired
    private TrackingDatasRepository trackingDatasRepository;
    // Nombre max de vues avant action (modifiable)
    private static final int MAX_VIEWS = 100;


    public Map<String, Object> generateGlobalStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total produits
        stats.put("totalProducts", produitRepository.count());

        // Total coupons envoyés et utilisés
        stats.put("totalCouponsSent", couponTrackingRepository.count());
        stats.put("totalCouponsUsed", couponTrackingRepository.countByStatus("used"));

        // Produits les plus visualisés
        List<Object[]> mostViewed = trackingDataRepository.findTopViewedProducts();
        stats.put("mostViewedProducts", mostViewed);

        // Produits ayant atteint max vues
        List<Long> triggeredProducts = getProductsReachedMaxViews();
        stats.put("productsReachedMaxViews", triggeredProducts);

        return stats;
    }
    //coupon used * nbr visite/total coupon
    public double computeEngagementScore() {
        long totalCouponsUsed = couponTrackingRepository.countByStatus("used");
        long totalCouponsSent = couponTrackingRepository.count();

        // Sum all views (alternative: fetch only the sum instead of per-product)
        long totalViews = trackingDataRepository.findAll()
                .stream()
                .count(); // Or use a dedicated query for efficiency

        // Compute: (totalCouponsUsed * totalViews) / totalCouponsSent
        return (double) (totalCouponsUsed * totalViews) / totalCouponsSent;
    }
    public List<Long> getProductsReachedMaxViews() {
    	List<Object[]> viewCounts = trackingDatasRepository.countViewsPerProduct();

        return viewCounts.stream()
                .filter(obj -> obj[1] != null && ((Number) obj[1]).intValue() >= MAX_VIEWS)
                .map(obj -> ((Number) obj[0]).longValue())
                .collect(Collectors.toList());
    }
    public Map<String, Object> getProductStats(Long productId) {
        Map<String, Object> stats = new HashMap<>();

        Long totalTime = trackingDataRepository.findByIdproduct(productId)
                .stream()
                .mapToLong(t -> t.getTimespent() != null ? t.getTimespent() : 0)
                .sum();

        long views = trackingDataRepository.countViewsByProduct(productId);

        // Moyenne temps passé
        Double avgTime = trackingDataRepository.findByIdproduct(productId)
                .stream()
                .filter(t -> t.getTimespent() != null)
                .mapToLong(TrackingData::getTimespent).average().orElse(0);

        stats.put("productId", productId);
        stats.put("totalViews", views);
        stats.put("totalTimeSpent", totalTime);
        stats.put("averageTimeSpent", avgTime);

        return stats;
    }
    public Map<String, Double> getMostFrequentPosition() {
        List<TrackingData> trackingList = trackingDataRepository.findAll();

        Map<String, Long> positionCount = trackingList.stream()
            .filter(t -> t.getLatitude() != null && t.getLongitude() != null)
            .map(t -> String.format("%.6f,%.6f", t.getLatitude(), t.getLongitude()))
            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        Optional<Map.Entry<String, Long>> mostCommon = positionCount.entrySet().stream()
            .max(Map.Entry.comparingByValue());

        return mostCommon.map(entry -> {
            String[] parts = entry.getKey().split(",");
            return Map.of(
                "latitude", Double.valueOf(parts[0]),
                "longitude", Double.valueOf(parts[1]),
                "count", entry.getValue().doubleValue()
            );
        }).orElse(Map.of());
    }

    
    
    public List<Map<String, Object>> getMostFrequentCategories() {
        List<TrackingData> trackingList = trackingDataRepository.findAll();

        Map<String, Long> categoryCount = trackingList.stream()
            .filter(t -> t.getIdproduct() != null)
            .map(t -> produitRepository.findByIdproduct(t.getIdproduct()).orElse(null))
            .filter(Objects::nonNull)
            .map(p -> p.getCategorie().getNom())
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        categoryCount.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("categorie", entry.getKey());
                map.put("count", entry.getValue());
                result.add(map);
            });

        return result;
    }
    public Map<String, Object> getPeakHourOfTracking() {
        List<TrackingData> trackingList = trackingDataRepository.findAll();

        // Grouper par heure (0 à 23) selon l'heure d'entrée
        Map<Integer, Long> hourCount = trackingList.stream()
            .filter(t -> t.getEntryTimeFormatted() != null)
            .map(t -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getEntryTimeFormatted());
                return cal.get(Calendar.HOUR_OF_DAY); // Extraire l'heure (0-23)
            })
            .collect(Collectors.groupingBy(hour -> hour, Collectors.counting()));

        // Identifier l’heure la plus fréquente
        Optional<Map.Entry<Integer, Long>> peak = hourCount.entrySet().stream()
            .max(Map.Entry.comparingByValue());

        Map<String, Object> result = new HashMap<>();
        peak.ifPresent(p -> {
            result.put("peakHour", p.getKey()); // 0 = minuit, 14 = 14h, etc.
            result.put("visitCount", p.getValue());
        });

        return result;
    }
    
    
    public List<Map<String, Object>> getTop5ProductsByStats() {
        List<Object[]> topProducts = trackingDataRepository.findTopViewedProducts();

        return topProducts.stream()
            .limit(5)
            .map(obj -> {
                Long productId = ((Number) obj[0]).longValue();
                Long views = ((Number) obj[1]).longValue();

                Optional<Produit> produitOpt = produitRepository.findByIdproduct(productId);
                if (produitOpt.isPresent()) {
                    Produit produit = produitOpt.get();
                    Map<String, Object> productInfo = new HashMap<>();
                    productInfo.put("id", produit.getIdproduct());
                    productInfo.put("name", produit.getDescription());
                    productInfo.put("category", produit.getCategorie().getNom());
                    productInfo.put("views", views);
                    return productInfo;
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }


//batch
    //fct total coupon total
    @Transactional
    @Scheduled(cron = "0 0 3 * * ?") // 🕒 tous les jours à 3h du matin
    public void renewTopProductDiscounts() {
        // 1. Trouver les top 5 produits par vues
        List<Object[]> topProducts = trackingDataRepository.findTopViewedProducts();
        List<Long> topProductIds = topProducts.stream()
            .map(obj -> ((Number) obj[0]).longValue())
            .limit(5)
            .toList();

        for (Long productId : topProductIds) {
            List<LinkProdDisc> links = linkProdDiscRepository.findByProductId(productId);

            for (LinkProdDisc link : links) {
                // Si la remise est expirée
                if (link.getValideTo().isBefore(LocalDate.now())) {
                    // Prolonger la validité + réinitialiser la quantité
                    link.setValideFrom(LocalDate.now());
                    link.setValideTo(LocalDate.now().plusDays(5)); // Ex: durée 5j
                    link.setQuantiteDiscount(10); // ou la même valeur initiale si connue

                    linkProdDiscRepository.save(link);
                }
            }
        }

        System.out.println("✅ Remises renouvelées automatiquement pour les produits top tracking");
    }


}
