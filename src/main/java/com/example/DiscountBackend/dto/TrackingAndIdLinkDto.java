package com.example.DiscountBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingAndIdLinkDto {

    private Long id;             // ID du tracking (si utilisé)
    private Long idproduct;      // ID du produit
    private Long idLink;         // ID du lien produit-remise

    private boolean active;
    private int duration;

    private LocalDate valideFrom; // ✅ corrigé pour correspondre à l'entité
    private LocalDate valideTo;   // ✅ idem

    private String priority;

    private Double latitude;
    private Double longitude;

    private Long timespent;
    private Integer clicks;

    private String description;  // ✅ nom conventionnel (camelCase)
}
