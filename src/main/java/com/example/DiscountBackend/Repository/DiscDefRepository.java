package com.example.DiscountBackend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.DiscountBackend.entities.DiscountDef;


public interface DiscDefRepository  extends JpaRepository<DiscountDef,Long> {
	 @Query("SELECT d FROM DiscountDef d WHERE d.valeur <= :price")
	    List<DiscountDef> findApplicableDiscounts(@Param("price") Double price);
	    boolean existsByRefDisc(String refDisc);

}

