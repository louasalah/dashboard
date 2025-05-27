package com.example.DiscountBackend.Controller;

import com.example.DiscountBackend.Services.IServiceCategorie;
import com.example.DiscountBackend.Services.IServiceProduit;
import com.example.DiscountBackend.Services.IServiceTracking;
import com.example.DiscountBackend.Services.ServiceProduit;
import com.example.DiscountBackend.dto.GeoLocationRequest;
import com.example.DiscountBackend.dto.ProductWithDiscountDTO;
import com.example.DiscountBackend.dto.ResponseMessage;
import com.example.DiscountBackend.entities.Categorie;
import com.example.DiscountBackend.entities.DiscountDef;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;
import com.example.DiscountBackend.entities.TrackingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })
@RestController
@RequestMapping("/api")
public class ProduitController {

    @Autowired
    private IServiceProduit servProduit;
    
    @Autowired

    private IServiceTracking servTrack;
    
    @Autowired

    private IServiceCategorie servCatego;

    @GetMapping("/all")
    public List<Produit> getAllProducts() {
        return servProduit.getAllProd();
    }
    @GetMapping("/products/{id}")
    public Produit getProductById(@PathVariable Long id) {
        return servProduit.getProdById(id);
    }

    @GetMapping("/getProdByIdWithDiscount/{id}")
    public  ProductWithDiscountDTO getProdByIdWithDiscount(@PathVariable Long id) {
        return servProduit.getProdByIdWithDiscount(id);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Produit> updateProduct(@PathVariable Long id, @RequestBody Produit Produit) {
	       
    	Produit existingProd = servProduit.getProdById(id);
       
    	existingProd.setReferenceProduct(Produit.getReferenceProduct());
    	existingProd.setDescription(Produit.getDescription());
    	existingProd.setComment(Produit.getComment());
    	existingProd.setPrice(Produit.getPrice());
    	existingProd.setQuantiteDiscount(Produit.getQuantiteDiscount());

    	Produit updateProduct = servProduit.updateProd(existingProd);

        return ResponseEntity.ok(updateProduct);
}


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    	servProduit.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/products/category/{nom}")
    public List<LinkProdDisc> getProductsByCategory(@PathVariable String nom) {
        System.out.println("Category received: " + nom); 
        
      
        
        // Log pour vérifierList<TrackingData> getcategoriesPRoduit(String nom);
        List<LinkProdDisc> products = servTrack.getCategoriesProduit(nom);
        System.out.println("Products found: " + (products != null ? products.size() : 0));
        return products;
    }
    @GetMapping("/products/search/description/{description}")
    public List<ProductWithDiscountDTO> searchProductsByDescriptionAndCategory(
        @RequestParam("nom") String nom,
        @PathVariable("description") String description) {

        // Recherche des produits associés à la catégorie et à la description
        return servProduit.findByCategoryAndDescription(nom,description);
    }
    @GetMapping("/products/prodDetail/{idproduct}")
    public ProductWithDiscountDTO getProductDetails(@PathVariable Long idproduct) {
        // Appeler la méthode de service qui retourne un seul DTO
        ProductWithDiscountDTO product = servProduit.getProductDetails(idproduct);
        return product;
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> addProduct(@RequestBody Produit Produit) {
        // Vérification si la référence de produit existe déjà
        if (servProduit.existsByReferenceProduct(Produit.getReferenceProduct())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("La référence de Produit existe déjà."));
        }

        try {
            // Vérification si la catégorie est bien envoyée et que l'ID catégorie n'est pas nul
            if (Produit.getCategorie() == null || Produit.getCategorie().getId_category() == null) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Catégorie manquante ou ID catégorie nul."));
            }

            // Afficher les valeurs pour le débogage
            System.out.println("Categorie envoyée : " + Produit.getCategorie());
            System.out.println("ID Categorie : " + Produit.getCategorie().getId_category());

            // Récupération de la catégorie depuis la base de données
            Categorie categorie = servCatego.getCategorieById(Produit.getCategorie().getId_category());
            if (categorie == null) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Catégorie introuvable avec l'ID : " + Produit.getCategorie().getId_category()));
            }

            // Création du nouveau produit
            Produit newProd = new Produit();
            newProd.setReferenceProduct(Produit.getReferenceProduct());
            newProd.setDescription(Produit.getDescription());
            newProd.setPrice(Produit.getPrice());
            newProd.setQuantiteDiscount(Produit.getQuantiteDiscount());
            newProd.setComment(Produit.getComment());
            newProd.setImage(Produit.getImage());
            newProd.setCategorie(categorie);

            // Sauvegarde du produit dans la base de données
            servProduit.addProduct(newProd);

            return ResponseEntity.status(200).body(new ResponseMessage("Produit ajouté avec succès."));
        } catch (Exception e) {
            // Log de l'erreur en console
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseMessage("Erreur interne du serveur. Veuillez réessayer plus tard."));
        }
    }

    }

