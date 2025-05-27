package com.example.DiscountBackend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiscountDef {
	@Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
	  private Long idDisc;  
	  @Column(unique = true) 
	  private String refDisc;
	  private String type;
	  private Double valeur;  
	 
	  // Relation Many-to-Many via LinkProdDisc
	    @OneToMany(mappedBy = "discountDef", cascade = CascadeType.ALL)
	    @JsonIgnore
	    private List<LinkProdDisc> linkProdDiscs;
	    public DiscountDef( String refDisc, String type, Double valeur) {
	        this.idDisc = idDisc;
	        this.refDisc = refDisc;
	        this.type = type;
	        this.valeur = valeur;
	       
	    }
}
