package com.example.DiscountBackend.Services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.DiscountBackend.dto.CreateLinkProductDTO;
import com.example.DiscountBackend.entities.LinkProdDisc;

public interface ISderviceLinkDiscDef {
	LinkProdDisc addLinkProdDisc(CreateLinkProductDTO LinkProdDisc) ;
	List<LinkProdDisc> getLinks();
	String prolongerLien(Long id, int jours, boolean active);
	ResponseEntity<String> updateLink(Long id, LinkProdDisc updatedData);
	  void deleteLink(Long idLink);
	  
	  // New method to get the most recent discount with 50% reduction
	    LinkProdDisc getMostRecentDiscountPreferFiftyPercent();
	

}
