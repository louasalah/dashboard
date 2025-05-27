package com.example.DiscountBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkProdDisc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLink;

    private boolean active;

				    private int duration;

    @Column(name = "valide_from", nullable = false)
    private LocalDate valideFrom;

    @Column(name = "valide_to", nullable = false)
    private LocalDate valideTo;

    private String priority;

    @Transient // ⚠️ Ce champ est temporaire, non stocké en base
    private int jours;

    private Integer quantiteDiscount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_product", nullable = false)
    private Produit product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_disc", nullable = false)
    private DiscountDef discountDef;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    // ✅ Expose uniquement l'ID du produit
    public Long getProductId() {
        return (product != null) ? product.getIdproduct() : null;
    }

    public Long getDiscountId() {
        return (discountDef != null) ? discountDef.getIdDisc() : null;
    }

    // ✅ Prix original du produit
    public Double getPrice() {
        return (product != null && product.getPrice() != null) ? product.getPrice() : 0.0;
    }
    public String getReferenceProduct() {
        return (product != null) ? product.getReferenceProduct() : null;
    }
    public String getrefDiscount() {
        return (discountDef != null) ? discountDef.getRefDisc() : null;
    }
    
 // ✅ Pourcentage de réduction (non stocké)
    @Transient
    private Double discountPercentage;

    public Double getDiscountPercentage() {
        if (discountDef != null && discountDef.getValeur() != null) {
            return discountDef.getValeur();
        }
        return null;
    }
}
