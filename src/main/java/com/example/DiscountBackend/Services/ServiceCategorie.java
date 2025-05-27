package com.example.DiscountBackend.Services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.CategoryRepository;
import com.example.DiscountBackend.entities.Categorie;

@Service

public class ServiceCategorie implements  IServiceCategorie {
	@Autowired
	private  CategoryRepository catRep;

	@Override
	public void addCategorie(Categorie c) {
		catRep.save(c);
		
	}

	@Override
	public List<Categorie> getAllCategories() {
		
		return catRep.findAll();
	}

	@Override
	public Categorie getCategorieById(Long idcategory) {
		
		return catRep.findById(idcategory).get();
	}

	@Override
	public void deleteCategorie(Long idcategory) {
		catRep.deleteById(idcategory);
	}

	

}
