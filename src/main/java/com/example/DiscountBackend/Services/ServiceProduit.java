package com.example.DiscountBackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.DiscDefRepository;
import com.example.DiscountBackend.Repository.ProduitRepository;
import com.example.DiscountBackend.dto.CreateLinkProductDTO;
import com.example.DiscountBackend.dto.GeoLocationRequest;
import com.example.DiscountBackend.dto.ProductWithDiscountDTO;
import com.example.DiscountBackend.entities.Categorie;
import com.example.DiscountBackend.entities.DiscountDef;
import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.entities.Produit;
import com.example.DiscountBackend.entities.TrackingData;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProduit implements IServiceProduit {

    @Autowired
    private ProduitRepository pr;

    @Autowired
    private DiscDefRepository discountRepo;
    @Override
    public List<Produit> getAllProd() {
        return pr.findAll();
    }

    @Override
    public Produit getProdById(Long id) {
        return pr.findById(id).get();  
    }
    @Override
    public ProductWithDiscountDTO getProdByIdWithDiscount(Long id) {
        // Get the product by ID
        Produit product = pr.findById(id).orElse(null);
        
        // If product not found, return null
        if (product == null) {
            return null;
        }
        
        ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
        dto.setIdproduct(product.getIdproduct());
        dto.setComment(product.getComment());
        dto.setDescription(product.getDescription());

        // Initialiser le prix original et le prix remisé
        double originalPrice = product.getPrice();
        double discountedPrice = 0.0;

        // Parcourir la table LinkProdDisc pour récupérer les prix et remises
        List<LinkProdDisc> discounts = product.getLinkProdDiscs();
        
        for (LinkProdDisc linkProdDisc : discounts) {
            if (originalPrice == 0.0) {
                originalPrice = linkProdDisc.getPrice(); // Récupérer le prix original
            }
            discountedPrice = linkProdDisc.getDiscountedPrice();  // Toujours mettre à jour le prix remisé
        }

        // Si aucun prix remisé n'est trouvé, utiliser le prix original
        if (discountedPrice == 0.0) {
            discountedPrice = originalPrice;
        }

        dto.setPrice(originalPrice);
        dto.setDiscountedPrice(discountedPrice);

        // Construire l'URL de l'image dynamiquement
        String imageUrl =  product.getImage();
        dto.setImageUrl(imageUrl);

        // Retourner directement l'objet DTO
        return dto;
    }
      
    
    @Override
    public Produit updateProd(Produit produit) {
        return pr.save(produit);
    }
    @Override
    public void deleteProduct(Long id) {
        pr.deleteByIdProduct(id);
    }
    @Override
    public List<ProductWithDiscountDTO> getAllProductsWithDiscount(String nom) {
        List<Produit> products;

        if (nom != null && !nom.isEmpty()) {
            products = pr.findByCategorieNom(nom); // Find products by category name
        } else {
            products = pr.findAll(); // Find all products
        }

        List<ProductWithDiscountDTO> productDTOs = new ArrayList<>();

        // For each product, get the discount details
        for (Produit product : products) {
            ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
            dto.setIdproduct(product.getIdproduct());
            dto.setDescription(product.getDescription());

            // Initialize the price and discounted price
          

            // Récupérer le prix original depuis l'entité Produit directement
            double originalPrice = product.getPrice();
            double discountedPrice = 0.0;

            // Parcourir les remises associées au produit s’il y en a
            List<LinkProdDisc> discounts = product.getLinkProdDiscs();

            for (LinkProdDisc linkProdDisc : discounts) {
                if (linkProdDisc.getDiscountedPrice() != 0.0) {
                    discountedPrice = linkProdDisc.getDiscountedPrice();  // On récupère un prix remisé valide
                    break; // si un seul prix suffit, sinon enlève ce break
                }
            }

            // Si aucun prix remisé n'est défini, utiliser le prix original
            if (discountedPrice == 0.0) {
                discountedPrice = originalPrice;
            }

            dto.setPrice(originalPrice);
            dto.setDiscountedPrice(discountedPrice);

            // Construire l'URL de l'image dynamiquement
            String imageUrl = product.getImage();
            dto.setImageUrl(imageUrl);

            System.out.println("test " + dto.getPrice());

        
            

            // Add the DTO to the list
            productDTOs.add(dto);
        }

        return productDTOs;
    }
    @Override
    public List<ProductWithDiscountDTO> findByCategoryAndDescription(String nom, String description) {
        List<Produit> products = new ArrayList<>();  // Initialisation par défaut

        // Si la description est spécifiée
        if (description != null && !description.isEmpty()) {
            // Recherche uniquement par description dans la catégorie spécifiée
            if (nom != null && !nom.isEmpty()) {
                // Recherche dans la catégorie avec description
                products = pr.findByCategorieNomAndProduitNom(nom, description);
            }
        } 
        // Si uniquement la catégorie est spécifiée
        else if (nom != null && !nom.isEmpty()) {
            // Recherche uniquement par catégorie si description est vide
            products = pr.findByCategorieNom(nom);
        } 
        // Si ni description ni catégorie ne sont spécifiées, récupérer tous les produits
        else {
            // Si aucun paramètre n'est spécifié, on récupère tous les produits
            products = pr.findAll();
        }

        List<ProductWithDiscountDTO> productDTOs = new ArrayList<>();

        // Pour chaque produit, récupérer les détails du discount
        for (Produit product : products) {
            ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
            dto.setIdproduct(product.getIdproduct());
            dto.setDescription(product.getDescription());

            // Initialiser le prix et le prix remisé
            double originalPrice = 0.0;
            double discountedPrice = 0.0;

            // Boucler à travers la table LinkProdDisc pour récupérer le prix et la remise
            List<LinkProdDisc> discounts = product.getLinkProdDiscs();
            for (LinkProdDisc linkProdDisc : discounts) {
                // Obtenir le prix original de LinkProdDisc (s'il est stocké là)
                if (originalPrice == 0.0) {
                    originalPrice = linkProdDisc.getPrice();  // Supposons que le prix est stocké dans LinkProdDisc
                }
                discountedPrice = linkProdDisc.getDiscountedPrice();  // Obtenez le prix remisé de LinkProdDisc
            }

            // Définir les prix
            dto.setPrice(originalPrice);
            dto.setDiscountedPrice(discountedPrice);

            // Construire l'URL de l'image dynamiquement
            String imageUrl = "http://localhost:8080/images/" + product.getIdproduct();
            dto.setImageUrl(imageUrl);

            // Ajouter le DTO à la liste
            productDTOs.add(dto);
            System.out.println("hhhhhhh"+productDTOs);
        }

        return productDTOs; // Retourner la liste de DTOs
    }

    @Override
    public ProductWithDiscountDTO getProductDetails(Long idproduct) {
        // Recherche un produit par son ID
        Produit product = pr.findById(idproduct).orElse(null); 

        if (product == null) {
            // Si aucun produit n'est trouvé, retourner null ou lancer une exception
            return null;
        }

        // Créer un DTO pour ce produit spécifique
        ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
        dto.setIdproduct(product.getIdproduct());
        dto.setDescription(product.getDescription());
        dto.setComment(product.getComment());

        // Récupérer le prix original depuis l'entité Produit directement
        double originalPrice = product.getPrice();
        double discountedPrice = 0.0;

        // Parcourir les remises associées au produit s’il y en a
        List<LinkProdDisc> discounts = product.getLinkProdDiscs();

        for (LinkProdDisc linkProdDisc : discounts) {
            if (linkProdDisc.getDiscountedPrice() != 0.0) {
                discountedPrice = linkProdDisc.getDiscountedPrice();  // On récupère un prix remisé valide
                break; // si un seul prix suffit, sinon enlève ce break
            }
        }

        // Si aucun prix remisé n'est défini, utiliser le prix original
        if (discountedPrice == 0.0) {
            discountedPrice = originalPrice;
        }

        dto.setPrice(originalPrice);
        dto.setDiscountedPrice(discountedPrice);

        // Construire l'URL de l'image dynamiquement
        String imageUrl = product.getImage();
        dto.setImageUrl(imageUrl);

        System.out.println("test " + dto.getPrice());

        // Retourner directement l'objet DTO
        return dto;
    }
    
    
    @Override
    public void addProduct(Produit Produit) {
         pr.save(Produit); 
    }
    @Override
	public boolean existsByReferenceProduct(String referenceProduct) {
		return pr.existsByReferenceProduct(referenceProduct);

	}

  
}