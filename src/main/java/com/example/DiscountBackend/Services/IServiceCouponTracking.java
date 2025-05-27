package com.example.DiscountBackend.Services;

import java.util.List;
import java.util.Map;

import com.example.DiscountBackend.entities.CouponTracking;

public interface IServiceCouponTracking {

	void saveTracking(String email, String couponCode, String productReference, String status, Long idLink);

	List<CouponTracking> getAllTrackings();

	  void saveTracking(String email, String productReference, String status, Long idLink);
	   void updateTrackingStatus(Long id, String newStatus);
	  
	   
	   //statistiques
	   Map<String, Object> getCouponStatistics();
	   
	   

}
