package com.example.DiscountBackend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.LinkProdDisc;

import jakarta.transaction.Transactional;

@Repository
public interface LinkDiscDefRepository  extends JpaRepository<LinkProdDisc,Long>{

	@Query("SELECT l FROM LinkProdDisc l LEFT JOIN l.product p LEFT JOIN p.categorie c WHERE c.nom = :nom")
	List<LinkProdDisc> findByProductByCategorieName(@Param("nom") String nom);



	
	@Query("SELECT l FROM LinkProdDisc l WHERE l.product.idproduct = :idproduct")
	List<LinkProdDisc> findByProductId(@Param("idproduct") Long idproduct);

	@Modifying
	@Transactional
	@Query("DELETE FROM LinkProdDisc p WHERE p.idLink = :idLink")
	void deleteByIdLink(@Param("idLink") Long id);

}
