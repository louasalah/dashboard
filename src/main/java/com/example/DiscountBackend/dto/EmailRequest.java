package com.example.DiscountBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailRequest {
	private String email;
	 private String couponCode; 
	    private String productReference; 
	    private Double discountValue;  
	    private Long timestamp;  
	    private String status; 
	    private Long idLink;   

}
