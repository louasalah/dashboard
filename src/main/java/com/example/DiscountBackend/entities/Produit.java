package com.example.DiscountBackend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproduct;

    private String referenceProduct;

    private String description;

    private String comment;
    
    private Double price;
    
    private Integer quantiteDiscount;

    private String image;
    
    

    
    @ManyToOne(cascade = CascadeType.ALL)  
    @JoinColumn(name = "id_category")
    //@JsonIgnore
    private Categorie categorie;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<LinkProdDisc> linkProdDiscs;

	
    
}
