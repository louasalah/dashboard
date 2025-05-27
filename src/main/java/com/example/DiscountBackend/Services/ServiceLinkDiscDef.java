package com.example.DiscountBackend.Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.DiscDefRepository;
import com.example.DiscountBackend.Repository.LinkDiscDefRepository;
import com.example.DiscountBackend.Repository.ProduitRepository;
import com.example.DiscountBackend.dto.CreateLinkProductDTO;
import com.example.DiscountBackend.entities.DiscountDef;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ServiceLinkDiscDef implements ISderviceLinkDiscDef {

    @Autowired
    private LinkDiscDefRepository LDP;

    @Autowired
    private ProduitRepository productRepo;

    @Autowired
    private DiscDefRepository discountRepo;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public LinkProdDisc addLinkProdDisc(CreateLinkProductDTO linkData) {
        Produit product = productRepo.findById(linkData.getIdproduct())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        DiscountDef discount = discountRepo.findById(linkData.getIdDisc())
                .orElseThrow(() -> new RuntimeException("Remise non trouvée"));

        LinkProdDisc link = new LinkProdDisc();
        link.setProduct(product);
        link.setDiscountDef(discount);
        link.setActive(false);
        link.setDuration(linkData.getDuration());
        link.setValideFrom(linkData.getValideFrom());
        link.setValideTo(linkData.getValideTo());
        link.setPriority(linkData.getPriority());

        // Calculer le prix remisé
        double discountValue = discount.getValeur();
        double discountedPrice = product.getPrice() * (1 - discountValue / 100);
        link.setDiscountedPrice(discountedPrice);

        LinkProdDisc saved = LDP.save(link);

        // Si actif, désactiver les autres remises concurrentes
        if (saved.isActive()) {
            entityManager.createNativeQuery(
                    "CALL deactivate_competing_discounts(:productId, :linkId, :priority, :fromDate, :toDate)")
                    .setParameter("productId", saved.getProductId())
                    .setParameter("linkId", saved.getIdLink())
                    .setParameter("priority", saved.getPriority())
                    .setParameter("fromDate", saved.getValideFrom())
                    .setParameter("toDate", saved.getValideTo())
                    .executeUpdate();
        }

        return saved;
    }
    
    @Override
    public void deleteLink(Long idLink) {
    	LDP.deleteByIdLink(idLink);
    }

    @Override
    public List<LinkProdDisc> getLinks() {
        return LDP.findAll();
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateLink(Long id, LinkProdDisc updatedData) {
        return LDP.findById(id).map(link -> {
            if (updatedData.getValideTo() != null) {
                link.setValideTo(updatedData.getValideTo());
            }
            link.setActive(true); // Forcer l’activation
            LDP.save(link);
            return ResponseEntity.ok("Lien mis à jour avec succès.");
        }).orElse(ResponseEntity.status(404).body("Lien non trouvé"));
    }

    @Override
    @Transactional
    public String prolongerLien(Long id, int jours, boolean active) {
        return LDP.findById(id).map(link -> {
            LocalDate currentValideTo = link.getValideTo();
            LocalDate newValidTo = currentValideTo.plusDays(jours);

            link.setValideTo(newValidTo);
            link.setActive(active);

            // Recalculer la durée
            long newDuration = ChronoUnit.DAYS.between(link.getValideFrom(), newValidTo);
            link.setDuration((int) newDuration);

            LDP.save(link);
            return "Remise prolongée jusqu’au " + newValidTo + " (+ " + jours + " jours), statut actif : " + active;
        }).orElseThrow(() -> new RuntimeException("Lien non trouvé"));
    }
    
    
    //home -50%
    @Override
    public LinkProdDisc getMostRecentDiscountPreferFiftyPercent() {
        // Get all links
        List<LinkProdDisc> allLinks = LDP.findAll();
        
        if (allLinks.isEmpty()) {
            return null;
        }
        
        // First, try to find discounts with exactly 50% reduction
        List<LinkProdDisc> fiftyPercentDiscounts = allLinks.stream()
                .filter(link -> {
                    DiscountDef discount = link.getDiscountDef();
                    return discount != null && discount.getValeur() == 50.0;
                })
                .collect(Collectors.toList());
        
        // If we found any 50% discounts, return the most recent one
        if (!fiftyPercentDiscounts.isEmpty()) {
            return fiftyPercentDiscounts.stream()
                    .max((a, b) -> a.getValideFrom().compareTo(b.getValideFrom()))
                    .orElse(null);
        }
        
        // If no 50% discounts found, return the most recent discount regardless of value
        return allLinks.stream()
                .max((a, b) -> a.getValideFrom().compareTo(b.getValideFrom()))
                .orElse(null);
    }
}
