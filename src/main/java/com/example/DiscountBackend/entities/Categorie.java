package com.example.DiscountBackend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Categorie {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_category;
	

  
	
    @Column(name = "nom")
	private String nom;
	@OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Produit> produits;

}