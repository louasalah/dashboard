package com.example.DiscountBackend.Services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.DiscountBackend.dto.GeoLocationRequest;
import com.example.DiscountBackend.dto.TrackingAndIdLinkDto;
import com.example.DiscountBackend.dto.TrackingLinkDTO;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.TrackingData;

public interface IServiceTracking {

    
   
    void enregistrerTrackingData(TrackingData data);
    public List<TrackingData> getTrackingData();
    List<TrackingAndIdLinkDto> getTrackingLinksById(Long idproduct);
	Optional<TrackingData> findByIdproductAndSessionId(Long idproduct, String sessionId);
	List<Integer> findLastClickCountByProductId(Long idproduct);
	List<TrackingLinkDTO> getTrackingLinkData();
	
	//statistiques 
	Map<String, Object> getAdvancedStatistics();
	
	List<LinkProdDisc> getCategoriesProduit(String nom);
	/*List<CategoryTrackingDTO> getTrackingDataGroupedByCategory();*/
    
	
	

}
