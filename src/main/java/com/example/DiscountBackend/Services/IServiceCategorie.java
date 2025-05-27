package com.example.DiscountBackend.Services;
import java.util.List;

import com.example.DiscountBackend.entities.Categorie;
import com.example.DiscountBackend.entities.Produit;

public interface IServiceCategorie {
	 void addCategorie(Categorie c);
	    List<Categorie> getAllCategories();
	    Categorie getCategorieById(Long categorieId);
	    void deleteCategorie(Long idCategory);
}