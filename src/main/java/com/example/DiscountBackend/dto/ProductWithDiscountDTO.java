package com.example.DiscountBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductWithDiscountDTO {
	private Long idproduct; // ID du produit
    private String description; // Nom du produit
    private String comment; 
    private Double Price; // Prix original
    private Double discountedPrice; // Prix après remise
    private String imageUrl; // URL de l'image

}
