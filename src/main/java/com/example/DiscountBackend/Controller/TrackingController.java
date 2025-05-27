package com.example.DiscountBackend.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List; // Import correct de List
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DiscountBackend.Repository.ProduitRepository;
import com.example.DiscountBackend.Services.ISderviceLinkDiscDef;
import com.example.DiscountBackend.Services.IServiceTracking;
import com.example.DiscountBackend.Services.ServiceClicks;
import com.example.DiscountBackend.dto.TrackingAndIdLinkDto;
import com.example.DiscountBackend.dto.TrackingLinkDTO;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;
import com.example.DiscountBackend.entities.TrackingData;

@CrossOrigin(origins = { "http://localhost:4200", "**" })
@RestController
@RequestMapping("/Api")
public class TrackingController {

    @Autowired
    private IServiceTracking servTrack;
    @Autowired
    private ISderviceLinkDiscDef LDP;
    @Autowired
    private ServiceClicks clickService;
    @Autowired
    private ProduitRepository prd;
    @PostMapping("/tracking/{idproduct}/{sessionId}")
    public ResponseEntity<Void> trackClientData(
            @PathVariable Long idproduct,
            @PathVariable String sessionId,
            @RequestBody TrackingData trackingData
            ) {

        // Vérifier si une ligne existe déjà pour ce produit et cette session
        Optional<TrackingData> existingTrackingOpt = servTrack.findByIdproductAndSessionId(idproduct, sessionId);

        if (existingTrackingOpt.isPresent()) {
        	  TrackingData existingTracking = existingTrackingOpt.get();
              
              // Ajouter le temps passé (timespent) de la nouvelle session au temps déjà existant
              long updatedTimeSpent = existingTracking.getTimespent() + trackingData.getTimespent();
              existingTracking.setTimespent(updatedTimeSpent);
              // Sauvegarder la mise à jour
              servTrack.enregistrerTrackingData(existingTracking);   
            
            System.out.println("Tracking Data Updated (Same Session): " + existingTracking);
        } else {
            // Nouvelle session - créer une nouvelle entrée
            LocalDateTime entryTime = LocalDateTime.now();
            Date formattedEntryTime = Date.from(entryTime.atZone(ZoneId.systemDefault()).toInstant());

            // Récupérer le dernier nombre de clics pour ce produit
            List<Integer> lastClicks = servTrack.findLastClickCountByProductId(idproduct);
            int lastClickCount = lastClicks.isEmpty() ? 0 : lastClicks.get(0);

       
            
            
            TrackingData newTrackingData = new TrackingData();
            newTrackingData.setIdproduct(idproduct);
            newTrackingData.setSessionId(sessionId);
            
            newTrackingData.setPagename(trackingData.getPagename());
            newTrackingData.setTimespent(trackingData.getTimespent());
            newTrackingData.setClicks(lastClickCount + 1); // Incrémente seulement pour nouvelle session
            newTrackingData.setEntryTimeFormatted(formattedEntryTime);
            newTrackingData.setLongitude(trackingData.getLongitude());
            newTrackingData.setLatitude(trackingData.getLatitude());
           
           
                
            // Enregistrer la nouvelle donnée
            servTrack.enregistrerTrackingData(newTrackingData);
            clickService.incrementClickCount(idproduct);
            

            System.out.println("New Session Tracking Data Saved: " + newTrackingData);
        }

        return ResponseEntity.ok().build();
    }
    
    
    @GetMapping("/all")
    public List<TrackingLinkDTO> getTrackingLinkData() {
        return servTrack.getTrackingLinkData();
    }


    // Méthode pour récupérer le dernier nombre de clics (à ajouter dans le service)
    public List<Integer> getLastClickCount(Long idproduct) {
        return servTrack.findLastClickCountByProductId(idproduct);
    }

    // Récupérer toutes les données de suivi avec le nombre de clics
    @GetMapping("/trackingData")
    public List<TrackingData> getTrackingData() {
    	
        List<TrackingData> trackingDataList = servTrack.getTrackingData();
        // Ajouter le nombre de clics pour chaque produit dans les données de suivi
        for (TrackingData data : trackingDataList) {
            Integer clickCount = clickService.getClickCount(data.getIdproduct());
            data.setClicks(clickCount); 
            // Mettre à jour le nombre de clics pour chaque produit
            
         // Récupérer le produit associé
            Produit produit = prd.findByIdproduct(data.getIdproduct()).orElse(null);
            if (produit != null && produit.getLinkProdDiscs() != null) {
                for (LinkProdDisc link : produit.getLinkProdDiscs()) {
                    if (link.isActive()) { // exemple : ne prendre que le lien actif
                        data.setIdLink(link.getIdLink());
                        break; // arrêter dès qu'on trouve un lien valide
                    }
                }
            }
        }
        return trackingDataList;
    }

    // Récupérer le nombre de clics pour un produit donné
    @GetMapping("/clicks/{idproduct}")
    public ResponseEntity<Integer> getClickCount(@PathVariable Long idproduct) {
        Integer clickCount = clickService.getClickCount(idproduct);
        return ResponseEntity.ok(clickCount);
    }

    // Incrémenter le nombre de clics pour un produit donné
    @PostMapping("/clicks/{idproduct}")
    public ResponseEntity<Void> incrementClickCount(@PathVariable Long idproduct) {
        // Appel au service pour incrémenter le nombre de clics
        clickService.incrementClickCount(idproduct);
        return ResponseEntity.ok().build();
    }


    // Récupérer les liens de suivi associés à un produit donné
    @GetMapping("/links/{idproduct}")
    public List<TrackingAndIdLinkDto> getTrackingLinksById(@PathVariable Long idproduct) {
        return servTrack.getTrackingLinksById(idproduct);
    }
    
    
    //statistiques
    @GetMapping("/advanced-statistics")
    public ResponseEntity<Map<String, Object>> getAdvancedTrackingStatistics() {
        return ResponseEntity.ok(servTrack.getAdvancedStatistics());
    }
    
    
    
    
    
    
    
    
    /*@GetMapping("/GroupedByCategory")
    public ResponseEntity<List<CategoryTrackingDTO>> getTrackingDataGroupedByCategory() {
        List<CategoryTrackingDTO> groupedData = servTrack.getTrackingDataGroupedByCategory();
        return ResponseEntity.ok(groupedData);
    }*/
    
    
    
}
