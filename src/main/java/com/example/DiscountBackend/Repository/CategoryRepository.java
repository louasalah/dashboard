package com.example.DiscountBackend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.Categorie;
@Repository
public interface CategoryRepository extends JpaRepository<Categorie,Long>{
	
}
