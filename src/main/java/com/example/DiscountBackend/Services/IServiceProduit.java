package com.example.DiscountBackend.Services;

import java.util.List;

import com.example.DiscountBackend.dto.GeoLocationRequest;
import com.example.DiscountBackend.dto.ProductWithDiscountDTO;
import com.example.DiscountBackend.entities.DiscountDef;
import com.example.DiscountBackend.entities.Produit;

public interface IServiceProduit {
	
    
    List<Produit> getAllProd();

    Produit getProdById(Long id);

    Produit updateProd(Produit Produit);

    void deleteProduct(Long id);
	List<ProductWithDiscountDTO> getAllProductsWithDiscount(String nom);

	List<ProductWithDiscountDTO> findByCategoryAndDescription(String nom, String description);
	ProductWithDiscountDTO getProductDetails(Long idproduct);

	ProductWithDiscountDTO getProdByIdWithDiscount(Long id);
	
	
    void addProduct(Produit Produit);  

	 boolean existsByReferenceProduct(String referenceProduct);
}
