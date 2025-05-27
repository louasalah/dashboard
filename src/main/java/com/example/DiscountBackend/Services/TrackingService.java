package com.example.DiscountBackend.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.ClickRepository;
import com.example.DiscountBackend.Repository.LinkDiscDefRepository;
import com.example.DiscountBackend.Repository.ProduitRepository;
import com.example.DiscountBackend.Repository.TrackingDataRepository;
import com.example.DiscountBackend.dto.TrackingAndIdLinkDto;
import com.example.DiscountBackend.dto.TrackingLinkDTO;
import com.example.DiscountBackend.entities.Click;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;
import com.example.DiscountBackend.entities.TrackingData;

@Service
public class TrackingService implements IServiceTracking {

    @Autowired
    private LinkDiscDefRepository LDP;
    @Autowired
    private ProduitRepository productRepository;

    @Autowired
    private TrackingDataRepository TraceDataRepository;

    @Autowired
    private ClickRepository clickRepository;

    @Override
    public void enregistrerTrackingData(TrackingData data) {
        TraceDataRepository.save(data);  // Correction pour utiliser TraceDataRepository
    }

    @Override
    public List<TrackingData> getTrackingData() {
        return TraceDataRepository.findAll();  // Correction pour utiliser TraceDataRepository
    }
    
    @Override
    public List<LinkProdDisc> getCategoriesProduit(String nom) {
        return LDP.findByProductByCategorieName(nom);
    }

    
    @Override
    public List<TrackingAndIdLinkDto> getTrackingLinksById(Long idproduct) {
        List<LinkProdDisc> productLinks = LDP.findByProductId(idproduct);
        if (productLinks == null || productLinks.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrackingData> trackingDataList = TraceDataRepository.findByIdproduct(idproduct);  // Correction pour utiliser TraceDataRepository
        List<TrackingAndIdLinkDto> dtoList = new ArrayList<>();

        // S'assurer que les données de suivi sont affichées une seule fois par produit
        for (LinkProdDisc link : productLinks) {
            TrackingAndIdLinkDto dto = new TrackingAndIdLinkDto();
            dto.setIdLink(link.getIdLink());
            dto.setIdproduct(link.getProductId());
            dto.setActive(link.isActive());
            dto.setDuration(link.getDuration());
            dto.setValideFrom(link.getValideFrom());
            dto.setValideTo(link.getValideTo());
            dto.setPriority(link.getPriority());
            dto.setIdLink(link.getIdLink());
            // Ajouter les informations de géolocalisation du produit
            if (!trackingDataList.isEmpty()) {
                TrackingData trackingData = trackingDataList.get(0);
                dto.setLatitude(trackingData.getLatitude());
                dto.setLongitude(trackingData.getLongitude());
                
            }

            // Récupérer le nombre de clics et l'ajouter au DTO
            Integer clickCount = getClickCount(link.getProductId());
            dto.setClicks(clickCount);

            dtoList.add(dto);
            
            
       

        }

        return dtoList;
    }
    
    
    
    
    
    @Override
    public List<TrackingLinkDTO> getTrackingLinkData() {
        List<LinkProdDisc> linkProdDiscList = LDP.findAll();
        List<TrackingData> trackingDataList = TraceDataRepository.findAll();

        // Combiner les données LinkProdDisc et TrackingData
        return linkProdDiscList.stream().flatMap(linkProdDisc -> 
            trackingDataList.stream().filter(trackingData -> 
                trackingData.getIdproduct().equals(linkProdDisc.getProductId())
            ).map(trackingData -> new TrackingLinkDTO(
                linkProdDisc.getIdLink(),
                linkProdDisc.isActive(),
                linkProdDisc.getDuration(),
                linkProdDisc.getValideFrom(),
                linkProdDisc.getValideTo(),
                linkProdDisc.getPriority(),
                linkProdDisc.getProductId(),
                linkProdDisc.getProduct().getDescription(),  
                linkProdDisc.getDiscountId(),
                linkProdDisc.getDiscountedPrice(),
                trackingData.getId(),
                trackingData.getIdproduct(),
                trackingData.getPagename(),
                trackingData.getTimespent(),
                trackingData.getClicks(),
                trackingData.getSessionId(),
                trackingData.getLatitude(),
                trackingData.getLongitude(),
             
                trackingData.getEntryTimeFormatted(),
                linkProdDisc.getProduct().getCategorie() != null ? linkProdDisc.getProduct().getCategorie().getNom() : null
            ))
        ).collect(Collectors.toList());
    }

    
    @Override
    public List<Integer> findLastClickCountByProductId(Long idproduct) {
        System.out.print(idproduct);
        
        // Récupère les derniers suivis
        List<TrackingData> lastTrackingData = TraceDataRepository
            .findTopByIdproductOrderByEntryTimeDesc(idproduct);
        
        // Transforme la liste en clics
        return lastTrackingData.stream()
            .map(TrackingData::getClicks)
            .collect(Collectors.toList());
    }

    
    @Override
    public Optional<TrackingData> findByIdproductAndSessionId(Long idproduct, String sessionId) {
        return TraceDataRepository.findByIdproductAndSessionId(idproduct, sessionId);
    }

    // Méthode corrigée pour récupérer les clics d'un produit
    private Integer getClickCount(Long idproduct) {
        Optional<Click> clickOpt = clickRepository.findByIdproduct(idproduct);
        return clickOpt.map(Click::getClicks).orElse(0);  // Retourner le nombre de clics ou 0 si aucun
    }
    
    //statistiques
    public Map<String, Object> getAdvancedStatistics() {
        Map<String, Object> stats = new HashMap<>();

        Long totalClicks = clickRepository.getTotalClicks();
        if (totalClicks == null) {
            totalClicks = 0L;
        }

        long totalTimeSpent = TraceDataRepository.getTotalTimeSpent();

        List<Object[]> mostClickedProducts = TraceDataRepository.findTopProductsByClicks();
        List<Object[]> mostViewedProducts = TraceDataRepository.findTopProductsByTimeSpent();

        // Create a list to store detailed product information
        List<Produit> productDetails = new ArrayList<>();

        // Iterate through most clicked and viewed products to fetch additional details
        for (Object[] clickedProduct : mostClickedProducts) {
        	Produit productInfo = fetchProductDetails(clickedProduct);
            productDetails.add(productInfo);
        }

        for (Object[] viewedProduct : mostViewedProducts) {
        	Produit productInfo = fetchProductDetails(viewedProduct);
            productDetails.add(productInfo);
        }

        stats.put("totalClicks", totalClicks);
        stats.put("totalTimeSpent", totalTimeSpent);
        stats.put("mostClickedProducts", mostClickedProducts);
        stats.put("mostViewedProducts", mostViewedProducts);
        stats.put("productDetails", productDetails);

        return stats;
    }

    // Helper method to fetch detailed product information
    private Produit fetchProductDetails(Object[] productData) {
    	Produit dto = new Produit();
        
        // Assuming the Object[] contains relevant product information
        // Modify these indices based on your actual repository method return structure
        if (productData.length > 0) {
            dto.setIdproduct(((Number) productData[0]).longValue());
            
            // Fetch additional details from your repositories
            // Example:
            dto.setReferenceProduct(productRepository.findProductReferenceById(dto.getIdproduct()));
           
            
            // Add more fields as needed from TrackingLinkDTO
           
        }
        
        return dto;
    }


    
    
    
    
    
    /*@Override
    public List<CategoryTrackingDTO> getTrackingDataGroupedByCategory() {
        return TraceDataRepository.getTrackingDataGroupedByCategory();
    }*/
}
