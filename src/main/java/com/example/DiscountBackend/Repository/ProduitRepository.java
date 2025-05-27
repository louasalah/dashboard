package com.example.DiscountBackend.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;

import jakarta.transaction.Transactional;
@Repository
public interface ProduitRepository extends JpaRepository<Produit,Long> {
	   @Query("SELECT p FROM Produit p WHERE p.categorie.nom = :nom")
	    List<Produit> findByCategorieNom(@Param("nom") String nom);
	@Modifying
	@Transactional
	@Query("DELETE FROM Produit p WHERE p.idproduct = :id")
	void deleteByIdProduct(@Param("id") Long id);

	    @Query("SELECT p FROM Produit p WHERE p.categorie.nom = :nom AND p.description LIKE %:description%")
	    List<Produit> findByCategorieNomAndProduitNom(@Param("nom") String categorieNom, @Param("description") String produitNom);

	    
	    
	    @Query("SELECT p FROM Produit p WHERE p.idproduct = :id")
	    Produit findProduitById(@Param("id") Long id);

	    @Query("SELECT p FROM Produit p WHERE p.categorie.nom = :description")
	    List<Produit> findByProduitNom(@Param("description") String description);
	    
	    
	    Optional<Produit> findByIdproduct(Long idproduct);

	    @Query("SELECT p.referenceProduct FROM Produit p WHERE p.idproduct = :productId")
	    String findProductReferenceById(@Param("productId") Long productId);

	    @Query("SELECT p.description FROM Produit p WHERE p.idproduct = :productId")
	    String findProductDescriptionById(@Param("productId") Long productId);
	    
	    
		boolean existsByReferenceProduct(String referenceProduct);

	   

	}



