package com.example.DiscountBackend.Controller;


import com.example.DiscountBackend.Services.IServiceCategorie;
import com.example.DiscountBackend.entities.Categorie;
import com.example.DiscountBackend.entities.Produit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private IServiceCategorie serviceCategorie;

   
    @GetMapping("/allcatego")
    public List<Categorie> getAllCategories() {
        return serviceCategorie.getAllCategories();
    }

    @GetMapping("/{id_category}")
    public Categorie getCategorieById(@PathVariable Long id) {
        return serviceCategorie.getCategorieById(id);
    }

    @PostMapping
    public void addCategorie(@RequestBody Categorie categorie) {
        serviceCategorie.addCategorie(categorie);
    }

    @DeleteMapping("/{id_category}")
    public void deleteCategorie(@PathVariable Long id) {
        serviceCategorie.deleteCategorie(id);
    }
}

